package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ActualizarEpisodioCommand(
        @NotNull
        @Min(1)
        Integer numeroEpisodio,

        @NotBlank
        @Size(max = 200)
        String titulo,

        @NotNull
        @Min(1)
        Integer duracionMinutos,

        @Size(max = 2000)
        String sinopsis
) {
}
