package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarCalificacionCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CalificarContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.CalificacionResponse;

import java.util.List;

public interface CalificacionService {

    CalificacionResponse calificar(CalificarContenidoCommand command);

    CalificacionResponse actualizar(Long calificacionId, ActualizarCalificacionCommand command);

    void eliminar(Long calificacionId);

    List<CalificacionResponse> listarPorContenido(Long contenidoId);
}
