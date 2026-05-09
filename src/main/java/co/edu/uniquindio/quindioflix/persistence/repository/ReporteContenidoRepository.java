package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.business.model.EstadoReporte;
import co.edu.uniquindio.quindioflix.persistence.entity.ReporteContenidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReporteContenidoRepository extends JpaRepository<ReporteContenidoEntity, Long> {

    Page<ReporteContenidoEntity> findAllByOrderByFechaReporteDesc(Pageable pageable);

    Page<ReporteContenidoEntity> findByEstadoOrderByFechaReporteDesc(EstadoReporte estado, Pageable pageable);

    @Modifying
    @Query(value = "UPDATE REPORTES_CONTENIDO SET ID_MODERADOR = NULL WHERE ID_MODERADOR = :usuarioId", nativeQuery = true)
    void clearModerador(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query(value = "DELETE FROM REPORTES_CONTENIDO WHERE ID_USUARIO_REPORTA = :usuarioId", nativeQuery = true)
    void deleteByUsuarioReportaId(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("DELETE FROM ReporteContenidoEntity r WHERE r.contenido.id = :contenidoId")
    void deleteByContenidoId(@Param("contenidoId") Long contenidoId);
}
