package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.service.ContenidoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/contenidos")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminContenidoController {

    private final ContenidoService contenidoService;

    @PostMapping("/actualizar-popularidad")
    @Operation(summary = "Recalcular popularidad", description = "Recalcula la popularidad de todo el catálogo usando reproducciones con avance mínimo del 90%.")
    public Map<String, Integer> actualizarPopularidad() {
        return Map.of("contenidosActualizados", contenidoService.actualizarPopularidadCatalogo());
    }
}
