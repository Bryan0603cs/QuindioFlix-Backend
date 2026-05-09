package co.edu.uniquindio.quindioflix.business.dto.response;

public record AuthResponse(
        String token,
        UsuarioResponse usuario
) {
}

