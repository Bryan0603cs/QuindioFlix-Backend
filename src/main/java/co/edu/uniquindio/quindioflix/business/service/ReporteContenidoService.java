package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.CrearReporteContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.ResolverReporteContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ReporteContenidoResponse;
import co.edu.uniquindio.quindioflix.business.model.EstadoReporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReporteContenidoService {

    ReporteContenidoResponse crear(Long usuarioReportaId, CrearReporteContenidoCommand command);

    ReporteContenidoResponse resolver(Long id, Long moderadorId, ResolverReporteContenidoCommand command);

    Page<ReporteContenidoResponse> listar(EstadoReporte estado, Pageable pageable);
}
