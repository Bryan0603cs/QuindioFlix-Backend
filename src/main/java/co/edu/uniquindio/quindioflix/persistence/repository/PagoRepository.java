package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.business.model.EstadoPago;
import co.edu.uniquindio.quindioflix.persistence.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PagoRepository extends JpaRepository<PagoEntity, Long> {

    List<PagoEntity> findByUsuarioIdOrderByFechaPagoDesc(Long usuarioId);

    Page<PagoEntity> findByUsuarioIdOrderByFechaPagoDesc(Long usuarioId, Pageable pageable);

    @Query("""
            SELECT COALESCE(SUM(p.monto), 0)
            FROM PagoEntity p
            WHERE p.usuario.id = :usuarioId
              AND p.estadoPago = :estado
            """)
    BigDecimal totalPagado(
            @Param("usuarioId") Long usuarioId,
            @Param("estado") EstadoPago estado
    );

    @Modifying
    @Query("DELETE FROM PagoEntity p WHERE p.usuario.id = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);
}
