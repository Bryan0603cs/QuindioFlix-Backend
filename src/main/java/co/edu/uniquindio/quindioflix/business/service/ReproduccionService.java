package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarAvanceReproduccionCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarReproduccionCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ReproduccionResponse;

import java.util.List;

public interface ReproduccionService {

    ReproduccionResponse registrar(RegistrarReproduccionCommand command);

    List<ReproduccionResponse> listarPorPerfil(Long perfilId);

    ReproduccionResponse actualizarAvance(Long reproduccionId, ActualizarAvanceReproduccionCommand command);
}
