package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.persistence.entity.FavoritoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoritoRepository extends JpaRepository<FavoritoEntity, Long> {

    boolean existsByPerfilIdAndContenidoId(Long perfilId, Long contenidoId);

    Optional<FavoritoEntity> findByPerfilIdAndContenidoId(Long perfilId, Long contenidoId);

    Page<FavoritoEntity> findByPerfilIdOrderByFechaAgregadoDesc(Long perfilId, Pageable pageable);

    List<FavoritoEntity> findByPerfilUsuarioIdOrderByFechaAgregadoDesc(Long usuarioId);

    @Modifying
    @Query(value = """
            DELETE FROM FAVORITOS
            WHERE ID_PERFIL IN (SELECT ID_PERFIL FROM PERFILES WHERE ID_USUARIO = :usuarioId)
            """, nativeQuery = true)
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Modifying
    @Query("DELETE FROM FavoritoEntity f WHERE f.perfil.id = :perfilId")
    void deleteByPerfilId(@Param("perfilId") Long perfilId);

    @Modifying
    @Query("DELETE FROM FavoritoEntity f WHERE f.contenido.id = :contenidoId")
    void deleteByContenidoId(@Param("contenidoId") Long contenidoId);
}
