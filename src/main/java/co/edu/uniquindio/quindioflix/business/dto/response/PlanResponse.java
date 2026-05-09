package co.edu.uniquindio.quindioflix.business.dto.response;

import java.math.*;

public record PlanResponse(
        Long id,
        String nombre,
        Integer pantallasSimultaneas,
        String calidad,
        BigDecimal precioMensual,
        Integer maxPerfiles
) {
}

