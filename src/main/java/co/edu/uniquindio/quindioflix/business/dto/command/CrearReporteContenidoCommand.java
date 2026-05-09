package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearReporteContenidoCommand(
        @NotNull
        Long contenidoId,

        @NotBlank
        @Size(max = 1000)
        String descripcion
) {
}
