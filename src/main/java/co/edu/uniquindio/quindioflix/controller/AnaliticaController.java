package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.service.AnaliticaService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reportes/analitica")
@RequiredArgsConstructor
public class AnaliticaController {

    private final AnaliticaService service;

    @GetMapping("/top-contenido-ciudad")
    @Operation(summary = "Top contenido por ciudad")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERADOR')")
    public List<AnaliticaService.TopContenido> top(
            @RequestParam String ciudad,
            @RequestParam(defaultValue = "10") int limite
    ) {
        return service.topCiudad(ciudad, limite);
    }

    @GetMapping("/ingresos-plan")
    @Operation(summary = "Ingresos por plan")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AnaliticaService.IngresoPlan> ingresos(
            @RequestParam int mes,
            @RequestParam int anio
    ) {
        return service.ingresos(mes, anio);
    }

    @GetMapping("/consumo-usuario")
    @Operation(summary = "Consumo por usuario")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERADOR')")
    public List<AnaliticaService.ConsumoUsuario> consumoUsuario(
            @RequestParam Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        return service.consumoUsuario(usuarioId, desde, hasta);
    }

    @GetMapping("/calificacion-genero")
    @Operation(summary = "Calificación promedio por género", description = "Calcula la calificación promedio por categoría filtrando por género.")
    @PreAuthorize("hasAnyRole('ADMIN', 'MODERADOR')")
    public List<AnaliticaService.CalificacionCategoria> calificacionGenero(
            @RequestParam String genero
    ) {
        return service.calificacionPorGenero(genero);
    }

}