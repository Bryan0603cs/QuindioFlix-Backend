package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarEpisodioCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearEpisodioCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.EpisodioResponse;

import java.util.List;

public interface EpisodioService {

    List<EpisodioResponse> listarPorTemporada(Long contenidoId, Long temporadaId);

    EpisodioResponse crear(Long contenidoId, Long temporadaId, CrearEpisodioCommand command);

    EpisodioResponse actualizar(Long contenidoId, Long temporadaId, Long episodioId, ActualizarEpisodioCommand command);

    void eliminar(Long contenidoId, Long temporadaId, Long episodioId);
}
