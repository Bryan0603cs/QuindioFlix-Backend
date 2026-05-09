package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.persistence.entity.ContenidoRelacionadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContenidoRelacionadoRepository extends JpaRepository<ContenidoRelacionadoEntity, Long> {

    List<ContenidoRelacionadoEntity> findByOrigenId(Long contenidoId);

    boolean existsByOrigenIdAndDestinoIdAndTipoRelacionIgnoreCase(
            Long origenId,
            Long destinoId,
            String tipoRelacion
    );

    @Modifying
    @Query("""
            DELETE FROM ContenidoRelacionadoEntity r
            WHERE r.origen.id = :contenidoId
               OR r.destino.id = :contenidoId
            """)
    void deleteByContenidoId(@Param("contenidoId") Long contenidoId);
}
