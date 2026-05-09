package co.edu.uniquindio.quindioflix.business.dto.response;

import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import co.edu.uniquindio.quindioflix.business.model.RolUsuario;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        String telefono,
        LocalDate fechaNacimiento,
        String ciudad,
        Long planId,
        String plan,
        EstadoCuenta estadoCuenta,
        LocalDateTime fechaRegistro,
        LocalDateTime fechaUltimoPago,
        LocalDateTime fechaVencimiento,
        Long referidoPorId,
        RolUsuario rol
) {
}
