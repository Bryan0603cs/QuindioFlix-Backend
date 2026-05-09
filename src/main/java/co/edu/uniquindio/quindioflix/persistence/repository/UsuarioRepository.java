package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import co.edu.uniquindio.quindioflix.business.model.RolUsuario;
import co.edu.uniquindio.quindioflix.persistence.entity.UsuarioEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<UsuarioEntity> findByEmailIgnoreCase(String email);

    List<UsuarioEntity> findByCiudadIgnoreCase(String ciudad);

    List<UsuarioEntity> findByReferidoPorIdOrderByFechaRegistroDesc(Long usuarioId);

    List<UsuarioEntity> findByEstadoCuentaAndFechaVencimientoBefore(
            EstadoCuenta estadoCuenta,
            LocalDateTime fechaLimite
    );

    @Query("""
            SELECT u
            FROM UsuarioEntity u
            JOIN FETCH u.plan
            LEFT JOIN FETCH u.referidoPor
            WHERE LOWER(u.email) = LOWER(:email)
            """)
    Optional<UsuarioEntity> findByEmailWithPlanAndReferente(@Param("email") String email);

    @Modifying
    @Query("UPDATE UsuarioEntity u SET u.referidoPor = null WHERE u.referidoPor.id = :usuarioId")
    void clearReferenciasReferidoPor(@Param("usuarioId") Long usuarioId);

    @Query("""
            SELECT u
            FROM UsuarioEntity u
            WHERE (:rol IS NULL OR u.rol = :rol)
              AND (:estado IS NULL OR u.estadoCuenta = :estado)
              AND (:planId IS NULL OR u.plan.id = :planId)
              AND (:ciudad IS NULL OR LOWER(u.ciudad) = LOWER(:ciudad))
            """)
    Page<UsuarioEntity> buscarUsuarios(@Param("rol") RolUsuario rol,
                                       @Param("estado") EstadoCuenta estado,
                                       @Param("planId") Long planId,
                                       @Param("ciudad") String ciudad,
                                       Pageable pageable);

}
