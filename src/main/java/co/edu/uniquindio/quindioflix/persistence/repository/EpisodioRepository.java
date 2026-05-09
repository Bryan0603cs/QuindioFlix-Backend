package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.persistence.entity.EpisodioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface EpisodioRepository extends JpaRepository<EpisodioEntity, Long> {

    @Query("""
            SELECT e
            FROM EpisodioEntity e
            WHERE e.temporada.id IN :temporadaIds
            ORDER BY e.temporada.numeroTemporada ASC, e.numeroEpisodio ASC
            """)
    List<EpisodioEntity> findByTemporadaIds(@Param("temporadaIds") Collection<Long> temporadaIds);

    List<EpisodioEntity> findByTemporadaIdOrderByNumeroEpisodioAsc(Long temporadaId);

    boolean existsByIdAndTemporadaId(Long episodioId, Long temporadaId);

    boolean existsByTemporadaIdAndNumeroEpisodio(Long temporadaId, Integer numeroEpisodio);

    @Modifying
    @Query("DELETE FROM EpisodioEntity e WHERE e.temporada.id = :temporadaId")
    void deleteByTemporadaId(@Param("temporadaId") Long temporadaId);

    @Modifying
    @Query(value = """
            DELETE FROM EPISODIOS
            WHERE ID_TEMPORADA IN (
                SELECT ID_TEMPORADA
                FROM TEMPORADAS
                WHERE ID_CONTENIDO = :contenidoId
            )
            """, nativeQuery = true)
    void deleteByContenidoId(@Param("contenidoId") Long contenidoId);
}
