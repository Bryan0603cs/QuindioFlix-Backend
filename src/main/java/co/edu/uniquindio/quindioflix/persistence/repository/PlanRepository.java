package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.persistence.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface PlanRepository extends JpaRepository<PlanEntity,Long> {
    Optional<PlanEntity> findByNombreIgnoreCase(String nombre);
    List<PlanEntity> findByActivoIgnoreCase(String activo);

}
