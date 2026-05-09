package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarEpisodioCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearEpisodioCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.EpisodioResponse;
import co.edu.uniquindio.quindioflix.business.service.EpisodioService;
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
@RequestMapping("/api/contenidos/{contenidoId}/temporadas/{temporadaId}/episodios")
@RequiredArgsConstructor
public class EpisodioController {

    private final EpisodioService service;

    @GetMapping
    public List<EpisodioResponse> listarPorTemporada(
            @PathVariable Long contenidoId,
            @PathVariable Long temporadaId
    ) {
        return service.listarPorTemporada(contenidoId, temporadaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','CONTENIDO')")
    public EpisodioResponse crear(
            @PathVariable Long contenidoId,
            @PathVariable Long temporadaId,
            @Valid @RequestBody CrearEpisodioCommand command
    ) {
        return service.crear(contenidoId, temporadaId, command);
    }

    @PutMapping("/{episodioId}")
    @PreAuthorize("hasAnyRole('ADMIN','CONTENIDO')")
    public EpisodioResponse actualizar(
            @PathVariable Long contenidoId,
            @PathVariable Long temporadaId,
            @PathVariable Long episodioId,
            @Valid @RequestBody ActualizarEpisodioCommand command
    ) {
        return service.actualizar(contenidoId, temporadaId, episodioId, command);
    }

    @DeleteMapping("/{episodioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','CONTENIDO')")
    public void eliminar(
            @PathVariable Long contenidoId,
            @PathVariable Long temporadaId,
            @PathVariable Long episodioId
    ) {
        service.eliminar(contenidoId, temporadaId, episodioId);
    }
}
