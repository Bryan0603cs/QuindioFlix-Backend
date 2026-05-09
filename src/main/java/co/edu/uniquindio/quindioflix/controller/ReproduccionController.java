package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarAvanceReproduccionCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarReproduccionCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ReproduccionResponse;
import co.edu.uniquindio.quindioflix.business.service.ReproduccionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Reproducciones")
@RequestMapping("/api/reproducciones")
@RequiredArgsConstructor
public class ReproduccionController {

    private final ReproduccionService service;

    @GetMapping
    @PreAuthorize("@authorizationService.canAccessPerfil(#perfilId)")
    public List<ReproduccionResponse> listarPorPerfil(@RequestParam Long perfilId) {
        return service.listarPorPerfil(perfilId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@authorizationService.canAccessPerfil(#command.perfilId())")
    public ReproduccionResponse registrar(@Valid @RequestBody RegistrarReproduccionCommand command) {
        return service.registrar(command);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@authorizationService.canAccessReproduccion(#id)")
    public ReproduccionResponse actualizarAvance(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarAvanceReproduccionCommand command
    ) {
        return service.actualizarAvance(id, command);
    }
}
