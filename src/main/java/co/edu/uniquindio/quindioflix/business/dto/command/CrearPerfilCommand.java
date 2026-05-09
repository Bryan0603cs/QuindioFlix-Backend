package co.edu.uniquindio.quindioflix.business.dto.command;

import co.edu.uniquindio.quindioflix.business.model.TipoPerfil;
import jakarta.validation.constraints.*;

public record CrearPerfilCommand(
        @NotBlank
        @Size(max = 60)
        String nombre,
        @Size(max = 255)
        String avatar,
        @NotNull
        TipoPerfil tipoPerfil
) {
}

