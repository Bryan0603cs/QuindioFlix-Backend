package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarAvanceReproduccionCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarReproduccionCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ReproduccionResponse;
import co.edu.uniquindio.quindioflix.business.service.ReproduccionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


@RestController
@Tag(name = "Reproducciones")
@RequestMapping("/api/reproducciones")
@RequiredArgsConstructor
public class ReproduccionController {

    private final ReproduccionService service;

    @GetMapping
    @PreAuthorize("@authorizationService.canAccessPerfil(#perfilId)")
    @Operation(summary = "Listar reproducciones por perfil", description = "Devuelve el historial paginado de reproducciones de un perfil autorizado.")
    public Page<ReproduccionResponse> listarPorPerfil(@RequestParam Long perfilId, Pageable pageable) {
        return service.listarPorPerfil(perfilId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@authorizationService.canAccessPerfil(#command.perfilId())")
    @Operation(summary = "Registrar reproducción", description = "Registra el consumo de contenido y actualiza la popularidad si corresponde.")
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
