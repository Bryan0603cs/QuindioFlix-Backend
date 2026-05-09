package co.edu.uniquindio.quindioflix.business.dto.response;

public record ContenidoRelacionadoResponse(
        Long id,
        Long contenidoOrigenId,
        Long contenidoDestinoId,
        String tituloDestino,
        String tipoRelacion,
        String descripcion
) {
}
