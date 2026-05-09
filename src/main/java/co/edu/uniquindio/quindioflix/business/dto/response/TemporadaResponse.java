package co.edu.uniquindio.quindioflix.business.dto.response;

import java.time.LocalDate;
import java.util.List;

public record TemporadaResponse(
        Long id,
        Long contenidoId,
        Integer numeroTemporada,
        String titulo,
        LocalDate fechaLanzamiento,
        List<EpisodioResponse> episodios
) {
}
