package co.edu.uniquindio.quindioflix.business.dto.command;

import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoUsuarioCommand(
        @NotNull(message = "El estado de cuenta es obligatorio")
        EstadoCuenta estadoCuenta
) {
}
