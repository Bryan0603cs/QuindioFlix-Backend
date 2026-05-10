package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.persistence.entity.ReproduccionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReproduccionRepository extends JpaRepository<ReproduccionEntity, Long> {

    Page<ReproduccionEntity> findByPerfilIdOrderByFechaHoraInicioDesc(Long perfilId, Pageable pageable);

    boolean existsByIdAndPerfilUsuarioId(Long reproduccionId, Long usuarioId);

    @Query("""
            SELECT COUNT(r) > 0
            FROM ReproduccionEntity r
            WHERE r.perfil.id = :perfilId
              AND r.contenido.id = :contenidoId
              AND r.porcentajeAvance >= :minimo
            """)
    boolean existeConAvanceMinimo(
            @Param("perfilId") Long perfilId,
            @Param("contenidoId") Long contenidoId,
            @Param("minimo") int minimo
    );

    @Query("""
            SELECT r
            FROM ReproduccionEntity r
            JOIN FETCH r.perfil p
            JOIN FETCH r.contenido c
            JOIN FETCH c.categoria
            WHERE p.usuario.id = :usuarioId
              AND r.fechaHoraInicio BETWEEN :desde AND :hasta
            ORDER BY p.nombre, c.categoria.nombre
            """)
    List<ReproduccionEntity> buscarConsumoUsuario(
            @Param("usuarioId") Long usuarioId,
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta
    );

    @Modifying
    @Query(value = """
            DELETE FROM REPRODUCCIONES
            WHERE ID_PERFIL IN (SELECT ID_PERFIL FROM PERFILES WHERE ID_USUARIO = :usuarioId)
            """, nativeQuery = true)
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("DELETE FROM ReproduccionEntity r WHERE r.contenido.id = :contenidoId")
    void deleteByContenidoId(@Param("contenidoId") Long contenidoId);

    @Modifying
    @Query("DELETE FROM ReproduccionEntity r WHERE r.episodio.id = :episodioId")
    void deleteByEpisodioId(@Param("episodioId") Long episodioId);

    @Modifying
    @Query(value = """
            DELETE FROM REPRODUCCIONES
            WHERE ID_EPISODIO IN (
                SELECT ID_EPISODIO
                FROM EPISODIOS
                WHERE ID_TEMPORADA = :temporadaId
            )
            """, nativeQuery = true)
    void deleteByTemporadaId(@Param("temporadaId") Long temporadaId);
    @Modifying
    @Query("DELETE FROM ReproduccionEntity r WHERE r.perfil.id = :perfilId")
    void deleteByPerfilId(@Param("perfilId") Long perfilId);

}
