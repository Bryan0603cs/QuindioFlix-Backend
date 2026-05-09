package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.*;

public record CambiarPlanCommand(
        @NotNull
        Long nuevoPlanId,
        @Size(max = 200)
        String motivo
) {
}

