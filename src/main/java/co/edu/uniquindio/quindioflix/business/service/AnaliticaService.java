package co.edu.uniquindio.quindioflix.business.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AnaliticaService {

    record TopContenido(Long contenidoId, String titulo, Long reproducciones) {
    }

    record IngresoPlan(String plan, BigDecimal total, Long pagos) {
    }

    record ConsumoUsuario(String perfil, String categoria, Long reproducciones, Long minutos) {
    }

    record CalificacionCategoria(String categoria, BigDecimal promedio, Long totalCalificaciones) {
    }

    List<TopContenido> topCiudad(String ciudad, int limite);

    List<IngresoPlan> ingresos(int mes, int anio);

    List<ConsumoUsuario> consumoUsuario(Long usuarioId, LocalDateTime desde, LocalDateTime hasta);

    List<CalificacionCategoria> calificacionPorGenero(String genero);
}
