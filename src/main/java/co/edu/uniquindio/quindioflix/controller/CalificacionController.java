package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarCalificacionCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CalificarContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.CalificacionResponse;
import co.edu.uniquindio.quindioflix.business.service.CalificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {

    private final CalificacionService service;

    @GetMapping
    public List<CalificacionResponse> listarPorContenido(@RequestParam Long contenidoId) {
        return service.listarPorContenido(contenidoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@authorizationService.canAccessPerfil(#command.perfilId())")
    public CalificacionResponse calificar(@Valid @RequestBody CalificarContenidoCommand command) {
        return service.calificar(command);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessCalificacion(#id)")
    public CalificacionResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarCalificacionCommand command
    ) {
        return service.actualizar(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("@authorizationService.canAccessCalificacion(#id)")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
