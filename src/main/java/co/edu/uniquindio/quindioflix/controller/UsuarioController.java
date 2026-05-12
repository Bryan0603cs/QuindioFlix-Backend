package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.command.CambiarEstadoUsuarioCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CambiarPlanCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CambiarRolUsuarioCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearPerfilCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.PagoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.PerfilResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.UsuarioResponse;
import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import co.edu.uniquindio.quindioflix.business.model.RolUsuario;
import co.edu.uniquindio.quindioflix.business.service.PerfilService;
import co.edu.uniquindio.quindioflix.business.service.UsuarioService;
import co.edu.uniquindio.quindioflix.configuration.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarios;
    private final PerfilService perfiles;

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Lista usuarios de forma paginada para panel administrativo con filtros opcionales. Usa sort=id,asc o sort=nombre,desc.")
    @PreAuthorize("hasAnyRole('ADMIN','MODERADOR')")
    public Page<UsuarioResponse> listar(
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Long planId,
            @RequestParam(required = false) String ciudad,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,asc") String sort
    ) {
        RolUsuario rolEnum = convertirRol(rol);
        EstadoCuenta estadoEnum = convertirEstado(estado);
        Pageable pageable = construirPageable(page, size, sort);
        return usuarios.listar(rolEnum, estadoEnum, planId, ciudad, pageable);
    }

    private RolUsuario convertirRol(String rol) {
        if (rol == null || rol.isBlank()) {
            return null;
        }
        return RolUsuario.valueOf(rol.trim().toUpperCase());
    }

    private EstadoCuenta convertirEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return null;
        }
        return EstadoCuenta.valueOf(estado.trim().toUpperCase());
    }

    private Pageable construirPageable(int page, int size, String sort) {
        int pagina = Math.max(page, 0);
        int tamano = size <= 0 || size > 50 ? 10 : size;

        String limpio = sort == null ? "id,asc" : sort
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .trim();

        String[] partes = limpio.split(",");
        String propiedad = partes.length > 0 ? partes[0].trim() : "id";

        if (!propiedad.matches("id|nombre|email|ciudad|fechaRegistro|fechaVencimiento")) {
            propiedad = "id";
        }

        Sort.Direction direccion = Sort.Direction.ASC;
        if (partes.length > 1 && partes[1].trim().equalsIgnoreCase("desc")) {
            direccion = Sort.Direction.DESC;
        }

        return PageRequest.of(pagina, tamano, Sort.by(direccion, propiedad));
    }

    @GetMapping("/me")
    @Operation(summary = "Consultar usuario autenticado")
    public UsuarioResponse me(@AuthenticationPrincipal AuthenticatedUser autenticado) {
        return usuarios.buscar(autenticado.usuarioId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessUser(#id)")
    public UsuarioResponse buscar(@PathVariable Long id) {
        return usuarios.buscar(id);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de cuenta", description = "Permite a ADMIN/MODERADOR activar, suspender o inactivar una cuenta.")
    @PreAuthorize("hasAnyRole('ADMIN','MODERADOR')")
    public UsuarioResponse cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambiarEstadoUsuarioCommand command
    ) {
        return usuarios.cambiarEstado(id, command);
    }

    @PatchMapping("/{id}/rol")
    @Operation(summary = "Cambiar rol de usuario", description = "Permite a ADMIN asignar roles CLIENTE, MODERADOR, CONTENIDO o ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    public UsuarioResponse cambiarRol(
            @PathVariable Long id,
            @Valid @RequestBody CambiarRolUsuarioCommand command
    ) {
        return usuarios.cambiarRol(id, command);
    }

    @PatchMapping("/{id}/plan")
    @PreAuthorize("@authorizationService.canAccessUser(#id)")
    public UsuarioResponse cambiarPlan(
            @PathVariable Long id,
            @Valid @RequestBody CambiarPlanCommand command
    ) {
        return usuarios.cambiarPlan(id, command);
    }

    @PostMapping("/{id}/perfiles")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@authorizationService.canAccessUser(#id)")
    public PerfilResponse crearPerfil(
            @PathVariable Long id,
            @Valid @RequestBody CrearPerfilCommand command
    ) {
        return perfiles.crear(id, command);
    }

    @GetMapping("/{id}/perfiles")
    @PreAuthorize("@authorizationService.canAccessUser(#id)")
    public List<PerfilResponse> perfiles(@PathVariable Long id) {
        return perfiles.listar(id);
    }

    @GetMapping("/{id}/favoritos")
    @PreAuthorize("@authorizationService.canAccessUser(#id)")
    public List<ContenidoResponse> favoritos(@PathVariable Long id) {
        return usuarios.favoritosDeUsuario(id);
    }

    @GetMapping("/{id}/pagos")
    @PreAuthorize("@authorizationService.canAccessUser(#id)")
    public Page<PagoResponse> pagos(@PathVariable Long id, Pageable pageable) {
        return usuarios.pagos(id, pageable);
    }

    @GetMapping("/{id}/referidos")
    @PreAuthorize("@authorizationService.canAccessUser(#id)")
    public List<UsuarioResponse> referidos(@PathVariable Long id) {
        return usuarios.referidosActivos(id);
    }

    @GetMapping("/{id}/referente")
    @PreAuthorize("@authorizationService.canAccessUser(#id)")
    public UsuarioResponse referente(@PathVariable Long id) {
        return usuarios.referenteDeUsuario(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@authorizationService.canAccessUser(#id)")
    public void eliminarCuenta(@PathVariable Long id) {
        usuarios.eliminarCuenta(id);
    }
}
