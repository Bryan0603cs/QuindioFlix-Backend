package co.edu.uniquindio.quindioflix.business.dto.command;

import co.edu.uniquindio.quindioflix.business.model.MetodoPago;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record RegistrarUsuarioCommand(
        @NotBlank
        @Size(max = 120)
        String nombre,

        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @NotBlank
        @Size(max = 30)
        String telefono,

        @NotNull
        @Past
        LocalDate fechaNacimiento,

        @NotBlank
        @Size(max = 80)
        String ciudad,

        @NotBlank
        @Size(min = 8, max = 80)
        String password,

        @NotNull
        Long planId,

        Long referidoPorId,

        @NotNull
        MetodoPago metodoPagoPrimerPago
) {
}
