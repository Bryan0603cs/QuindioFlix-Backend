package co.edu.uniquindio.quindioflix.controller;

import io.swagger.v3.oas.annotations.Operation;
import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearRelacionContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoRelacionadoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;
import co.edu.uniquindio.quindioflix.business.service.ContenidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contenidos")
@RequiredArgsConstructor
public class ContenidoController {

    private final ContenidoService service;

    @GetMapping
    @Operation(summary = "Listar catálogo")
    public Page<ContenidoResponse> listar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Long categoriaId,
            @RequestParam(required = false) Long generoId,
            Pageable pageable
    ) {
        return service.listar(titulo, categoriaId, generoId, pageable);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar contenido")
    public ContenidoResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @GetMapping("/{id}/relacionados")
    public List<ContenidoRelacionadoResponse> relacionados(@PathVariable Long id) {
        return service.relacionados(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','CONTENIDO')")
    @Operation(summary = "Crear contenido")
    public ContenidoResponse crear(@Valid @RequestBody CrearContenidoCommand command) {
        return service.crear(command);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','CONTENIDO')")
    @Operation(summary = "Actualizar contenido")
    public ContenidoResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarContenidoCommand command
    ) {
        return service.actualizar(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','CONTENIDO')")
    @Operation(summary = "Eliminar contenido")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    @PostMapping("/{id}/relacionados")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','CONTENIDO')")
    public ContenidoRelacionadoResponse agregarRelacionado(
            @PathVariable Long id,
            @Valid @RequestBody CrearRelacionContenidoCommand command
    ) {
        return service.agregarRelacionado(id, command);
    }
}
