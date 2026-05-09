package co.edu.uniquindio.quindioflix.business.dto.response;

import co.edu.uniquindio.quindioflix.business.model.TipoContenido;

public record CategoriaResponse(
        Long id,
        String nombre,
        TipoContenido tipoContenido
) {
}
