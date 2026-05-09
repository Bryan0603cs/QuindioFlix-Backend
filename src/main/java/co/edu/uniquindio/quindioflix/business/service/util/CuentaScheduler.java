package co.edu.uniquindio.quindioflix.business.service.util;

import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import co.edu.uniquindio.quindioflix.persistence.entity.UsuarioEntity;
import co.edu.uniquindio.quindioflix.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CuentaScheduler {

    private final UsuarioRepository usuarios;

    @Scheduled(cron = "${app.scheduler.cuentas-vencidas-cron:0 0 2 * * *}")
    @Transactional
    public void desactivarCuentasVencidas() {
        int total = desactivarCuentasVencidasInterno();
        log.info("Scheduler de cuentas vencidas ejecutado. Cuentas desactivadas={}", total);
    }

    @Transactional
    public int desactivarCuentasVencidasManual() {
        int total = desactivarCuentasVencidasInterno();
        log.warn("Desactivación manual de cuentas vencidas ejecutada. Cuentas desactivadas={}", total);
        return total;
    }

    private int desactivarCuentasVencidasInterno() {
        LocalDateTime limite = LocalDateTime.now().minusDays(30);
        List<UsuarioEntity> vencidos = usuarios.findByEstadoCuentaAndFechaVencimientoBefore(
                EstadoCuenta.ACTIVO,
                limite
        );

        vencidos.forEach(usuario -> usuario.setEstadoCuenta(EstadoCuenta.INACTIVO));
        return vencidos.size();
    }
}
