package co.edu.uniquindio.quindioflix.business.dto.command;

import co.edu.uniquindio.quindioflix.business.model.EstadoReporte;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResolverReporteContenidoCommand(
        @NotNull
        EstadoReporte estado,

        @Size(max = 1000)
        String comentarioResolucion
) {
}
