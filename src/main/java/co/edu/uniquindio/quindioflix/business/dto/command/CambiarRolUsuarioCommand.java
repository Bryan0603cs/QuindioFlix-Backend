package co.edu.uniquindio.quindioflix.business.dto.command;

import co.edu.uniquindio.quindioflix.business.model.RolUsuario;
import jakarta.validation.constraints.NotNull;

public record CambiarRolUsuarioCommand(
        @NotNull(message = "El rol es obligatorio")
        RolUsuario rol
) {
}
