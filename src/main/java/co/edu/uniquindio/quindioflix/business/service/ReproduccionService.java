package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarAvanceReproduccionCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarReproduccionCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ReproduccionResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReproduccionService {

    ReproduccionResponse registrar(RegistrarReproduccionCommand command);

    Page<ReproduccionResponse> listarPorPerfil(Long perfilId, Pageable pageable);

    ReproduccionResponse actualizarAvance(Long reproduccionId, ActualizarAvanceReproduccionCommand command);
}
