package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.CrearPerfilCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.EditarPerfilCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.PerfilResponse;

import java.util.List;

public interface PerfilService {

    PerfilResponse crear(Long usuarioId, CrearPerfilCommand command);

    PerfilResponse actualizar(Long perfilId, EditarPerfilCommand command);

    List<PerfilResponse> listar(Long usuarioId);

    void eliminar(Long perfilId);
}
