package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.service.ContenidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Tag(name = "Administración de contenido")
@RequestMapping("/api/admin/contenidos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN') and @authorizationService.isCurrentUserActive()")
public class AdminContenidoController {

    private final ContenidoService contenidoService;

    @PostMapping("/actualizar-popularidad")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Recalcula la popularidad del catálogo",
            description = "Actualiza el campo POPULARIDAD de cada contenido según reproducciones completas con avance mayor o igual al 90%.")
    public Map<String, Integer> actualizarPopularidad() {
        return Map.of("contenidosActualizados", contenidoService.actualizarPopularidad());
    }
}
