package co.edu.uniquindio.quindioflix.controller;

import io.swagger.v3.oas.annotations.Operation;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearReporteContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.ResolverReporteContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ReporteContenidoResponse;
import co.edu.uniquindio.quindioflix.business.model.EstadoReporte;
import co.edu.uniquindio.quindioflix.business.service.ReporteContenidoService;
import co.edu.uniquindio.quindioflix.configuration.security.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/reportes-contenido")
@RequiredArgsConstructor
public class ReporteContenidoController {

    private final ReporteContenidoService service;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERADOR')")
    @Operation(summary = "Listar reportes")
    public Page<ReporteContenidoResponse> listar(@org.springframework.web.bind.annotation.RequestParam(required = false) EstadoReporte estado, Pageable pageable) {
        return service.listar(estado, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear reporte de contenido")
    public ReporteContenidoResponse crear(@AuthenticationPrincipal AuthenticatedUser user, @Valid @RequestBody CrearReporteContenidoCommand command) {
        return service.crear(user.usuarioId(), command);
    }

    @PatchMapping("/{id}/resolver")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERADOR')")
    @Operation(summary = "Resolver reporte")
    public ReporteContenidoResponse resolver(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser user,
            @Valid @RequestBody ResolverReporteContenidoCommand command
    ) {
        return service.resolver(id, user.usuarioId(), command);
    }
}
