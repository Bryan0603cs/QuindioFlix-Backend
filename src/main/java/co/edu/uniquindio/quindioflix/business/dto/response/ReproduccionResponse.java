package co.edu.uniquindio.quindioflix.business.dto.response;

import co.edu.uniquindio.quindioflix.business.model.Dispositivo;
import java.time.*;

public record ReproduccionResponse(
        Long id,
        Long perfilId,
        Long contenidoId,
        Long episodioId,
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin,
        Dispositivo dispositivo,
        Integer porcentajeAvance
) {
}

