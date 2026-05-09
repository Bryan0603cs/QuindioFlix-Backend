package co.edu.uniquindio.quindioflix.business.dto.response;

import co.edu.uniquindio.quindioflix.business.model.TipoPerfil;

public record PerfilResponse(
        Long id,
        Long usuarioId,
        String nombre,
        String avatar,
        TipoPerfil tipoPerfil
) {
}

