package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.*;

public record CrearReporteContenidoCommand(
        @NotNull
        Long contenidoId,
        @NotBlank
        @Size(max = 1000)
        String descripcion
) {
}
