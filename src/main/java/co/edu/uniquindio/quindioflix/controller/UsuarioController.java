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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@Tag(name = "Usuarios")
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarios;
    private final PerfilService perfiles;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MODERADOR') and @authorizationService.isCurrentUserActive()")
    public Page<UsuarioResponse> listar(
            @RequestParam(required = false) RolUsuario rol,
            @RequestParam(required = false) EstadoCuenta estado,
            @RequestParam(required = false) Long planId,
            @RequestParam(required = false) String ciudad,
            Pageable pageable
    ) {
        return usuarios.listar(rol, estado, planId, ciudad, pageable);
    }

    @GetMapping("/me")
    @PreAuthorize("@authorizationService.isCurrentUserActive()")
    public UsuarioResponse me(@AuthenticationPrincipal AuthenticatedUser autenticado) {
        return usuarios.buscar(autenticado.usuarioId());
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessUser(#id)")
    public UsuarioResponse buscar(@PathVariable Long id) {
        return usuarios.buscar(id);
    }

    @PatchMapping("/{id}/plan")
    @PreAuthorize("@authorizationService.canAccessUser(#id)")
    public UsuarioResponse cambiarPlan(
            @PathVariable Long id,
            @Valid @RequestBody CambiarPlanCommand command
    ) {
        return usuarios.cambiarPlan(id, command);
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN','MODERADOR') and @authorizationService.isCurrentUserActive()")
    public UsuarioResponse cambiarEstado(@PathVariable Long id,
                                         @Valid @RequestBody CambiarEstadoUsuarioCommand command) {
        return usuarios.cambiarEstado(id, command.estadoCuenta());
    }

    @PatchMapping("/{id}/rol")
    @PreAuthorize("hasRole('ADMIN') and @authorizationService.isCurrentUserActive()")
    public UsuarioResponse cambiarRol(@PathVariable Long id,
                                      @Valid @RequestBody CambiarRolUsuarioCommand command) {
        return usuarios.cambiarRol(id, command.rol());
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
        return usuarios.pagosDeUsuario(id, pageable);
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
