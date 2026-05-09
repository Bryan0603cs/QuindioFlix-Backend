package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.service.util.CuentaScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/cuentas")
@RequiredArgsConstructor
public class AdminCuentaController {

    private final CuentaScheduler cuentaScheduler;

    @PostMapping("/desactivar-vencidas")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public String desactivarVencidas() {
        int total = cuentaScheduler.desactivarCuentasVencidasManual();
        return "Cuentas desactivadas: " + total;
    }
}