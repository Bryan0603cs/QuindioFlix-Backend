package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.service.AnaliticaService;
import co.edu.uniquindio.quindioflix.persistence.entity.ReproduccionEntity;
import co.edu.uniquindio.quindioflix.persistence.repository.ReproduccionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnaliticaServiceImpl implements AnaliticaService {

    private final ReproduccionRepository reproducciones;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<TopContenido> topCiudad(String ciudad, int limite) {
        String sql = """
                SELECT c.ID_CONTENIDO, c.TITULO, COUNT(*) AS TOTAL
                FROM REPRODUCCIONES r
                JOIN PERFILES p ON p.ID_PERFIL = r.ID_PERFIL
                JOIN USUARIOS u ON u.ID_USUARIO = p.ID_USUARIO
                JOIN CONTENIDO c ON c.ID_CONTENIDO = r.ID_CONTENIDO
                WHERE UPPER(u.CIUDAD) = UPPER(?1)
                GROUP BY c.ID_CONTENIDO, c.TITULO
                ORDER BY TOTAL DESC
                """;

        Query query = entityManager.createNativeQuery(sql)
                .setParameter(1, ciudad);
        query.setMaxResults(Math.max(1, limite));

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(this::mapTopContenido)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<IngresoPlan> ingresos(int mes, int año) {
        String sql = """
                SELECT pl.NOMBRE, COALESCE(SUM(pa.MONTO), 0), COUNT(pa.ID_PAGO)
                FROM PAGOS pa
                JOIN USUARIOS u ON u.ID_USUARIO = pa.ID_USUARIO
                JOIN PLANES pl ON pl.ID_PLAN = u.ID_PLAN
                WHERE pa.ESTADO_PAGO = 'EXITOSO'
                  AND EXTRACT(MONTH FROM pa.FECHA_PAGO) = ?1
                  AND EXTRACT(YEAR FROM pa.FECHA_PAGO) = ?2
                GROUP BY pl.NOMBRE
                ORDER BY pl.NOMBRE
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter(1, mes)
                .setParameter(2, año)
                .getResultList();

        return rows.stream()
                .map(this::mapIngresoPlan)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsumoUsuario> consumoUsuario(Long usuarioId, LocalDateTime desde, LocalDateTime hasta) {
        List<ReproduccionEntity> historial = reproducciones.buscarConsumoUsuario(usuarioId, desde, hasta);

        Map<String, List<ReproduccionEntity>> agrupado = historial.stream()
                .collect(Collectors.groupingBy(
                        claveConsumo(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return agrupado.entrySet()
                .stream()
                .map(entry -> mapConsumoUsuario(entry.getKey(), entry.getValue()))
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public List<CalificacionGenero> calificacionPromedioPorCategoriaGenero(String genero) {
        String sql = """
                SELECT cat.NOMBRE, ROUND(AVG(cal.ESTRELLAS), 2) AS PROMEDIO, COUNT(cal.ID_CALIFICACION) AS TOTAL
                FROM CALIFICACIONES cal
                JOIN CONTENIDO c ON c.ID_CONTENIDO = cal.ID_CONTENIDO
                JOIN CATEGORIAS cat ON cat.ID_CATEGORIA = c.ID_CATEGORIA
                JOIN CONTENIDO_GENERO cg ON cg.ID_CONTENIDO = c.ID_CONTENIDO
                JOIN GENEROS g ON g.ID_GENERO = cg.ID_GENERO
                WHERE UPPER(g.NOMBRE) = UPPER(?1)
                GROUP BY cat.NOMBRE
                ORDER BY cat.NOMBRE
                """;

        @SuppressWarnings("unchecked")
        List<Object[]> rows = entityManager.createNativeQuery(sql)
                .setParameter(1, genero)
                .getResultList();

        return rows.stream()
                .map(this::mapCalificacionGenero)
                .toList();
    }

    private TopContenido mapTopContenido(Object[] row) {
        return new TopContenido(
                ((Number) row[0]).longValue(),
                (String) row[1],
                ((Number) row[2]).longValue()
        );
    }


    private CalificacionGenero mapCalificacionGenero(Object[] row) {
        return new CalificacionGenero(
                (String) row[0],
                toBigDecimal(row[1]),
                ((Number) row[2]).longValue()
        );
    }

    private IngresoPlan mapIngresoPlan(Object[] row) {
        return new IngresoPlan(
                (String) row[0],
                toBigDecimal(row[1]),
                ((Number) row[2]).longValue()
        );
    }

    private Function<ReproduccionEntity, String> claveConsumo() {
        return reproduccion -> reproduccion.getPerfil().getNombre()
                + "|"
                + reproduccion.getContenido().getCategoria().getNombre();
    }

    private ConsumoUsuario mapConsumoUsuario(String clave, List<ReproduccionEntity> reproducciones) {
        String[] partes = clave.split("\\|", 2);
        long minutos = reproducciones.stream()
                .mapToLong(this::calcularMinutos)
                .sum();

        return new ConsumoUsuario(
                partes[0],
                partes[1],
                (long) reproducciones.size(),
                minutos
        );
    }

    private long calcularMinutos(ReproduccionEntity reproduccion) {
        if (reproduccion.getFechaHoraFin() == null) {
            return 0;
        }

        return Math.max(0, Duration.between(
                reproduccion.getFechaHoraInicio(),
                reproduccion.getFechaHoraFin()
        ).toMinutes());
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal bigDecimal) {
            return bigDecimal;
        }

        if (value instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }

        return BigDecimal.ZERO;
    }
}
