package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarTemporadaCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearTemporadaCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.TemporadaResponse;

import java.util.List;

public interface TemporadaService {

    List<TemporadaResponse> listarPorContenido(Long contenidoId);

    TemporadaResponse crear(Long contenidoId, CrearTemporadaCommand command);

    TemporadaResponse actualizar(Long contenidoId, Long temporadaId, ActualizarTemporadaCommand command);

    void eliminar(Long contenidoId, Long temporadaId);
}
