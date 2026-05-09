package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearRelacionContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoRelacionadoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ContenidoService {

    Page<ContenidoResponse> listar(String titulo, Long categoriaId, Long generoId, Pageable pageable);

    ContenidoResponse buscar(Long id);

    ContenidoResponse crear(CrearContenidoCommand command);

    ContenidoResponse actualizar(Long id, ActualizarContenidoCommand command);

    void eliminar(Long id);

    List<ContenidoRelacionadoResponse> relacionados(Long contenidoId);

    ContenidoRelacionadoResponse agregarRelacionado(Long contenidoId, CrearRelacionContenidoCommand command);

    int actualizarPopularidadCatalogo();
}
