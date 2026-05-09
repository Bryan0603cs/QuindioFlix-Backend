package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.exception.BusinessException;
import co.edu.uniquindio.quindioflix.business.service.AnaliticaService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Analítica")
@RequestMapping("/api/reportes/analitica")
@RequiredArgsConstructor
@PreAuthorize("@authorizationService.canModerate()")
public class AnaliticaController {

    private final AnaliticaService service;

    @GetMapping("/top-contenido-ciudad")
    public List<AnaliticaService.TopContenido> top(
            @RequestParam String ciudad,
            @RequestParam(defaultValue = "10") int limite
    ) {
        return service.topCiudad(ciudad, limite);
    }

    @GetMapping("/ingresos-plan")
    @PreAuthorize("hasRole('ADMIN') and @authorizationService.isCurrentUserActive()")
    public List<AnaliticaService.IngresoPlan> ingresos(
            @RequestParam int mes,
            @RequestParam(required = false) Integer anio,
            @RequestParam(name = "año", required = false) Integer año
    ) {
        return service.ingresos(mes, resolverAnio(anio, año));
    }

    @GetMapping("/calificacion-genero")
    public List<AnaliticaService.CalificacionGenero> calificacionGenero(@RequestParam String genero) {
        return service.calificacionPromedioPorCategoriaGenero(genero);
    }

    @GetMapping("/consumo-usuario")
    public List<AnaliticaService.ConsumoUsuario> consumoUsuario(
            @RequestParam Long usuarioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta
    ) {
        return service.consumoUsuario(usuarioId, desde, hasta);
    }

    private int resolverAnio(Integer anio, Integer año) {
        Integer valor = anio != null ? anio : año;
        if (valor == null) {
            throw new BusinessException("MISSING_YEAR", "Debe enviar el parámetro anio o año.");
        }
        return valor;
    }
}
