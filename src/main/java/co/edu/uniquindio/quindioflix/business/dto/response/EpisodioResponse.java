package co.edu.uniquindio.quindioflix.business.dto.response;

public record EpisodioResponse(
        Long id,
        Long temporadaId,
        Integer numeroEpisodio,
        String titulo,
        Integer duracionMinutos,
        String sinopsis
) {
}
