package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.persistence.entity.CalificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalificacionRepository extends JpaRepository<CalificacionEntity, Long> {

    boolean existsByPerfilIdAndContenidoId(Long perfilId, Long contenidoId);

    boolean existsByIdAndPerfilUsuarioId(Long calificacionId, Long usuarioId);

    List<CalificacionEntity> findByContenidoIdOrderByFechaCalificacionDesc(Long contenidoId);

    @Modifying
    @Query(value = """
            DELETE FROM CALIFICACIONES
            WHERE ID_PERFIL IN (SELECT ID_PERFIL FROM PERFILES WHERE ID_USUARIO = :usuarioId)
            """, nativeQuery = true)
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("DELETE FROM CalificacionEntity c WHERE c.contenido.id = :contenidoId")
    void deleteByContenidoId(@Param("contenidoId") Long contenidoId);
    @Modifying
    @Query("DELETE FROM CalificacionEntity c WHERE c.perfil.id = :perfilId")
    void deleteByPerfilId(@Param("perfilId") Long perfilId);

}
