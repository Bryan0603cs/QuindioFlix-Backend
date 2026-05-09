package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.persistence.entity.TemporadaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TemporadaRepository extends JpaRepository<TemporadaEntity, Long> {

    List<TemporadaEntity> findByContenidoIdOrderByNumeroTemporadaAsc(Long contenidoId);

    Optional<TemporadaEntity> findByIdAndContenidoId(Long temporadaId, Long contenidoId);

    boolean existsByIdAndContenidoId(Long temporadaId, Long contenidoId);

    boolean existsByContenidoIdAndNumeroTemporada(Long contenidoId, Integer numeroTemporada);

    @Modifying
    @Query("DELETE FROM TemporadaEntity t WHERE t.contenido.id = :contenidoId")
    void deleteByContenidoId(@Param("contenidoId") Long contenidoId);
}
