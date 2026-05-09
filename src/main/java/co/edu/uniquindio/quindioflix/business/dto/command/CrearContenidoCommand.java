package co.edu.uniquindio.quindioflix.business.dto.command;

import co.edu.uniquindio.quindioflix.business.model.ClasificacionEdad;
import jakarta.validation.constraints.*;
import java.util.*;

public record CrearContenidoCommand(
        @NotNull
        Long categoriaId,
        @NotBlank
        @Size(max = 200)
        String titulo,
        @NotNull
        @Min(1900)
        @Max(value = 2030, message = "El año no puede ser tan lejano en el futuro")
        Integer añoLanzamiento,
        @NotNull
        @Min(1)
        Integer duracionMinutos,
        @Size(max = 2000)
        String sinopsis,
        @NotNull
        ClasificacionEdad clasificacionEdad,
        @NotNull
        Boolean originalQuindioflix,
        @NotNull
        Long empleadoResponsableId,
        @NotEmpty
        List<Long> generoIds
) {
}

