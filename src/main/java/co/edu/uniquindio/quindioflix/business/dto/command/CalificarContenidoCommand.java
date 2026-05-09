package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.*;

public record CalificarContenidoCommand(
        @NotNull
        Long perfilId,
        @NotNull
        Long contenidoId,
        @NotNull
        @Min(1)
        @Max(5)
        Integer estrellas,
        @Size(max = 1000)
        String resena
) {
}

