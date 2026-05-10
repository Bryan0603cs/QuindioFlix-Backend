package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarTemporadaCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearTemporadaCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.TemporadaResponse;
import co.edu.uniquindio.quindioflix.business.service.TemporadaService;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/contenidos/{contenidoId}/temporadas")
@RequiredArgsConstructor
public class TemporadaController {

    private final TemporadaService service;

    @GetMapping
    @PreAuthorize("@authorizationService.isCurrentUserActive()")
    public List<TemporadaResponse> listarPorContenido(@PathVariable Long contenidoId) {
        return service.listarPorContenido(contenidoId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','CONTENIDO')")
    public TemporadaResponse crear(
            @PathVariable Long contenidoId,
            @Valid @RequestBody CrearTemporadaCommand command
    ) {
        return service.crear(contenidoId, command);
    }

    @PutMapping("/{temporadaId}")
    @PreAuthorize("hasAnyRole('ADMIN','CONTENIDO')")
    public TemporadaResponse actualizar(
            @PathVariable Long contenidoId,
            @PathVariable Long temporadaId,
            @Valid @RequestBody ActualizarTemporadaCommand command
    ) {
        return service.actualizar(contenidoId, temporadaId, command);
    }

    @DeleteMapping("/{temporadaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','CONTENIDO')")
    public void eliminar(@PathVariable Long contenidoId, @PathVariable Long temporadaId) {
        service.eliminar(contenidoId, temporadaId);
    }
}
