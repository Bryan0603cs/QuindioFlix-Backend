package co.edu.uniquindio.quindioflix.business.dto.response;

import co.edu.uniquindio.quindioflix.business.model.*;
import java.math.*;
import java.time.*;

public record PagoResponse(
        Long id,
        Long usuarioId,
        LocalDateTime fechaPago,
        BigDecimal monto,
        MetodoPago metodoPago,
        EstadoPago estadoPago,
        String referencia,
        BigDecimal descuentoAplicado
) {
}

