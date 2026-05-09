package co.edu.uniquindio.quindioflix.business.dto.command;

import co.edu.uniquindio.quindioflix.business.model.ClasificacionEdad;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ActualizarContenidoCommand(
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
