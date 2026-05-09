package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarPagoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.PagoResponse;
import co.edu.uniquindio.quindioflix.business.service.PagoService;
import co.edu.uniquindio.quindioflix.configuration.security.AuthenticatedUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Pagos")
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService service;

    @GetMapping
    @PreAuthorize("@authorizationService.canAccessUser(#usuarioId)")
    public Page<PagoResponse> listarPorUsuario(@RequestParam Long usuarioId, Pageable pageable) {
        return service.listarPorUsuario(usuarioId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@authorizationService.isCurrentUserActive()")
    public PagoResponse registrar(@AuthenticationPrincipal AuthenticatedUser autenticado,
                                  @Valid @RequestBody RegistrarPagoCommand command) {
        return service.registrar(autenticado.usuarioId(), command);
    }
}
