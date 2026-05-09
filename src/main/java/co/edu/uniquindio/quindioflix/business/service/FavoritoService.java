package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.AgregarFavoritoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;

import java.util.List;

public interface FavoritoService {

    Long agregar(AgregarFavoritoCommand command);

    List<ContenidoResponse> listarPorPerfil(Long perfilId);

    void eliminar(Long perfilId, Long contenidoId);
}
