package co.edu.uniquindio.quindioflix.business.dto.response;

import java.time.*;

public record CalificacionResponse(
        Long id,
        Long perfilId,
        Long contenidoId,
        Integer estrellas,
        String resena,
        LocalDateTime fechaCalificacion
) {
}

