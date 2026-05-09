package co.edu.uniquindio.quindioflix.business.dto.response;

public record EmpleadoResponse(
        Long id,
        String nombre,
        String email,
        String cargo,
        Long departamentoId,
        String departamento
) {
}
