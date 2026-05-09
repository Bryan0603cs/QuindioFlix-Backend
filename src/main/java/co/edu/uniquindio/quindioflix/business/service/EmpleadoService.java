package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.response.EmpleadoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmpleadoService {

    Page<EmpleadoResponse> listar(Pageable pageable);
}
