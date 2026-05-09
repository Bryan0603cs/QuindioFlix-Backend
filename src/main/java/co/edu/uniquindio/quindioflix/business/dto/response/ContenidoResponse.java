package co.edu.uniquindio.quindioflix.business.dto.response;

import co.edu.uniquindio.quindioflix.business.model.ClasificacionEdad;
import java.time.*;
import java.util.*;

public record ContenidoResponse(
        Long id,
        Long categoriaId,
        String categoria,
        String titulo,
        Integer añoLanzamiento,
        Integer duracionMinutos,
        String sinopsis,
        ClasificacionEdad clasificacionEdad,
        LocalDate fechaAgregado,
        Boolean originalQuindioflix,
        Long empleadoResponsableId,
        Integer popularidad,
        List<String> generos
) {
}

