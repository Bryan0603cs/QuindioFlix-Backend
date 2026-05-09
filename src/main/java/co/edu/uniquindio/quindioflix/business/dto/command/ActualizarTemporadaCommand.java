package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ActualizarTemporadaCommand(
        @NotNull
        @Min(1)
        Integer numeroTemporada,

        @NotBlank
        @Size(max = 160)
        String titulo,

        LocalDate fechaLanzamiento
) {
}
