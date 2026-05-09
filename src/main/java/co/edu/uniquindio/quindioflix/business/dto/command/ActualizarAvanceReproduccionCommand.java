package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ActualizarAvanceReproduccionCommand(
        @NotNull
        @Min(0)
        @Max(100)
        Integer porcentajeAvance,
        LocalDateTime fechaHoraFin
) {
}
