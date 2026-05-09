package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.*;

public record AgregarFavoritoCommand(
        @NotNull
        Long perfilId,
        @NotNull
        Long contenidoId
) {
}

