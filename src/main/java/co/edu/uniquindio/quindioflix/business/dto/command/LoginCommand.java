package co.edu.uniquindio.quindioflix.business.dto.command;

import jakarta.validation.constraints.*;

public record LoginCommand(
        @NotBlank
        @Email
        String email,
        @NotBlank
        String password
) {
}

