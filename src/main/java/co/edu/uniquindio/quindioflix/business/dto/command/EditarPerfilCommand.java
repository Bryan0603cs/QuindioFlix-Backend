package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditarPerfilCommand(
        @NotBlank
        @Size(max = 60)
        String nombre,

        @Size(max = 255)
        String avatar
) {
}
