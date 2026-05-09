package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CrearRelacionContenidoCommand(
        @NotNull
        Long contenidoDestinoId,

        @NotBlank
        @Size(max = 60)
        String tipoRelacion,

        @Size(max = 500)
        String descripcion
) {
}
