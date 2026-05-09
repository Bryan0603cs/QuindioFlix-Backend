package co.edu.uniquindio.quindioflix.persistence.repository;

import co.edu.uniquindio.quindioflix.persistence.entity.GeneroEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GeneroRepository extends JpaRepository<GeneroEntity, Long> {

    List<GeneroEntity> findAllByOrderByNombreAsc();
}
