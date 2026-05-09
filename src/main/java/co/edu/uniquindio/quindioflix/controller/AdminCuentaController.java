package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.service.util.CuentaScheduler;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Administración de cuentas")
@RequestMapping("/api/admin/cuentas")
@RequiredArgsConstructor
public class AdminCuentaController {

    private final CuentaScheduler cuentaScheduler;

    @PostMapping("/desactivar-vencidas")
    @PreAuthorize("hasRole('ADMIN') and @authorizationService.isCurrentUserActive()")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Integer> desactivarVencidas() {
        return Map.of("cuentasDesactivadas", cuentaScheduler.desactivarCuentasVencidasManual());
    }
}
