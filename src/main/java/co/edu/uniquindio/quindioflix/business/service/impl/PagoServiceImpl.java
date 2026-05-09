package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarPagoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.PagoResponse;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import co.edu.uniquindio.quindioflix.business.model.EstadoPago;
import co.edu.uniquindio.quindioflix.business.service.PagoService;
import co.edu.uniquindio.quindioflix.persistence.entity.PagoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.UsuarioEntity;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.PagoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements PagoService {

    private static final BigDecimal REFERRED_DISCOUNT_RATE = new BigDecimal("0.10");

    private final PagoRepository pagos;
    private final UsuarioRepository usuarios;

    @Override
    @Transactional
    public PagoResponse registrar(Long usuarioId, RegistrarPagoCommand command) {
        UsuarioEntity usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioId));

        BigDecimal montoBase = usuario.getPlan().getPrecioMensual().setScale(2, RoundingMode.HALF_UP);
        BigDecimal descuento = calcularDescuentoReferido(usuario, montoBase);
        BigDecimal montoFinal = montoBase.subtract(descuento).setScale(2, RoundingMode.HALF_UP);
        LocalDateTime fechaPago = LocalDateTime.now();

        PagoEntity pago = PagoEntity.builder()
                .usuario(usuario)
                .fechaPago(fechaPago)
                .monto(montoFinal)
                .metodoPago(command.metodoPago())
                .estadoPago(EstadoPago.EXITOSO)
                .referencia(command.referencia())
                .descuentoAplicado(descuento)
                .build();

        pago = pagos.save(pago);

        if (pago.getEstadoPago() == EstadoPago.EXITOSO) {
            usuario.setEstadoCuenta(EstadoCuenta.ACTIVO);
            usuario.setFechaUltimoPago(fechaPago);
            usuario.setFechaVencimiento(fechaPago.plusDays(30));
        }

        log.info(
                "Pago registrado: id={}, usuario={}, estado={}, monto={}, descuento={}",
                pago.getId(),
                usuario.getId(),
                pago.getEstadoPago(),
                pago.getMonto(),
                pago.getDescuentoAplicado()
        );
        return MapperService.pago(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagoResponse> listarPorUsuario(Long usuarioId, Pageable pageable) {
        if (!usuarios.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario", usuarioId);
        }

        return pagos.findByUsuarioIdOrderByFechaPagoDesc(usuarioId, pageable)
                .map(MapperService::pago);
    }

    private BigDecimal calcularDescuentoReferido(UsuarioEntity usuario, BigDecimal montoBase) {
        if (usuario.getReferidoPor() == null || !usuario.getReferidoPor().activo()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return montoBase.multiply(REFERRED_DISCOUNT_RATE).setScale(2, RoundingMode.HALF_UP);
    }
}
