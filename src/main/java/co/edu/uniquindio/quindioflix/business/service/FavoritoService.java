package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.AgregarFavoritoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoritoService {

    Long agregar(AgregarFavoritoCommand command);

    Page<ContenidoResponse> listarPorPerfil(Long perfilId, Pageable pageable);

    void eliminar(Long perfilId, Long contenidoId);
}
