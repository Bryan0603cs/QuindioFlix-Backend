package co.edu.uniquindio.quindioflix.business.dto.response;

import co.edu.uniquindio.quindioflix.business.model.EstadoReporte;
import java.time.*;

public record ReporteContenidoResponse(
        Long id,
        Long usuarioReportaId,
        Long contenidoId,
        String descripcion,
        LocalDateTime fechaReporte,
        EstadoReporte estado,
        Long moderadorId,
        LocalDateTime fechaResolucion,
        String comentarioResolucion
) {
}

