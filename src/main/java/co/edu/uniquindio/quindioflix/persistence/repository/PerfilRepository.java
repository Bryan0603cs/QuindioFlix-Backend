package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.persistence.entity.PerfilEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PerfilRepository extends JpaRepository<PerfilEntity, Long> {

    long countByUsuarioId(Long usuarioId);

    boolean existsByIdAndUsuarioId(Long perfilId, Long usuarioId);

    List<PerfilEntity> findByUsuarioId(Long usuarioId);

    @Modifying
    @Query("DELETE FROM PerfilEntity p WHERE p.usuario.id = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);
}
