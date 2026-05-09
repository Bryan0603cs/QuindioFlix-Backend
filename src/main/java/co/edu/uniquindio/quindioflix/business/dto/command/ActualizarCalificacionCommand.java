package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActualizarCalificacionCommand(
        @NotNull
        @Min(1)
        @Max(5)
        Integer estrellas,

        @Size(max = 1000)
        String resena
) {
}
