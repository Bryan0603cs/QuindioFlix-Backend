package co.edu.uniquindio.quindioflix.business.dto.command;

import co.edu.uniquindio.quindioflix.business.model.Dispositivo;
import jakarta.validation.constraints.*;
import java.time.*;

public record RegistrarReproduccionCommand(
        @NotNull
        Long perfilId,
        @NotNull
        Long contenidoId,
        Long episodioId,
        @NotNull
        LocalDateTime fechaHoraInicio,
        LocalDateTime fechaHoraFin,
        @NotNull
        Dispositivo dispositivo,
        @NotNull
        @Min(0)
        @Max(100)
        Integer porcentajeAvance
) {
}

