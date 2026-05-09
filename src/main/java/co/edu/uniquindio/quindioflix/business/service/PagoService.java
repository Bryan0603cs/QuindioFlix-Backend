package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarPagoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.PagoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PagoService {

    PagoResponse registrar(Long usuarioId, RegistrarPagoCommand command);

    Page<PagoResponse> listarPorUsuario(Long usuarioId, Pageable pageable);
}
