package co.edu.uniquindio.quindioflix.business.service.util;

import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import co.edu.uniquindio.quindioflix.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class CuentaScheduler {

    private final UsuarioRepository usuarioRepository;

    @Scheduled(cron = "${CUENTAS_VENCIDAS_CRON:0 0 2 * * *}")
    @Transactional
    public void desactivarCuentasVencidas() {
        int total = desactivarCuentasVencidasInterno();
        log.info("Scheduler ejecutado. Cuentas vencidas desactivadas: {}", total);
    }

    @Transactional
    public int desactivarCuentasVencidasManual() {
        int total = desactivarCuentasVencidasInterno();
        log.warn("Desactivación manual ejecutada por administrador. Cuentas desactivadas: {}", total);
        return total;
    }

    private int desactivarCuentasVencidasInterno() {
        LocalDateTime limite = LocalDateTime.now().minusDays(30);

        return usuarioRepository.desactivarCuentasVencidas(
                limite,
                EstadoCuenta.ACTIVO,
                EstadoCuenta.INACTIVO
        );
    }
}