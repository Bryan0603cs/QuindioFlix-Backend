package co.edu.uniquindio.quindioflix.business.dto.command;

import co.edu.uniquindio.quindioflix.business.model.MetodoPago;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegistrarPagoCommand(
        @NotNull
        MetodoPago metodoPago,

        @Size(max = 120)
        String referencia
) {
}
