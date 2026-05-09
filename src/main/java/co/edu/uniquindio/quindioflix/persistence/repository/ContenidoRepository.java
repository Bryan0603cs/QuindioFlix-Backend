package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.persistence.entity.ContenidoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContenidoRepository extends JpaRepository<ContenidoEntity, Long> {

    @Query(
            value = """
                    SELECT DISTINCT c
                    FROM ContenidoEntity c
                    LEFT JOIN c.generos g
                    WHERE (:titulo IS NULL OR LOWER(c.titulo) LIKE LOWER(:titulo))
                      AND (:categoriaId IS NULL OR c.categoria.id = :categoriaId)
                      AND (:generoId IS NULL OR g.id = :generoId)
                    """,
            countQuery = """
                    SELECT COUNT(DISTINCT c)
                    FROM ContenidoEntity c
                    LEFT JOIN c.generos g
                    WHERE (:titulo IS NULL OR LOWER(c.titulo) LIKE LOWER(:titulo))
                      AND (:categoriaId IS NULL OR c.categoria.id = :categoriaId)
                      AND (:generoId IS NULL OR g.id = :generoId)
                    """
    )
    Page<ContenidoEntity> buscarCatalogo(
            @Param("titulo") String titulo,
            @Param("categoriaId") Long categoriaId,
            @Param("generoId") Long generoId,
            Pageable pageable
    );

    @Modifying
    @Query(value = "DELETE FROM CONTENIDO_GENERO WHERE ID_CONTENIDO = :contenidoId", nativeQuery = true)
    void deleteGenerosByContenidoId(@Param("contenidoId") Long contenidoId);

    @Modifying
    @Query(value = """
            UPDATE CONTENIDO c
            SET c.POPULARIDAD = (
                SELECT COUNT(*)
                FROM REPRODUCCIONES r
                WHERE r.ID_CONTENIDO = :contenidoId
                  AND r.PORCENTAJE_AVANCE >= 90
            )
            WHERE c.ID_CONTENIDO = :contenidoId
            """, nativeQuery = true)
    void recalcularPopularidadContenido(@Param("contenidoId") Long contenidoId);

    @Modifying
    @Query(value = """
            UPDATE CONTENIDO c
            SET c.POPULARIDAD = (
                SELECT COUNT(*)
                FROM REPRODUCCIONES r
                WHERE r.ID_CONTENIDO = c.ID_CONTENIDO
                  AND r.PORCENTAJE_AVANCE >= 90
            )
            """, nativeQuery = true)
    int recalcularPopularidadCatalogo();
}
