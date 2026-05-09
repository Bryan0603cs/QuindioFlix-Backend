package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.command.CrearReporteContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.ResolverReporteContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ReporteContenidoResponse;
import co.edu.uniquindio.quindioflix.business.exception.BusinessException;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.model.EstadoReporte;
import co.edu.uniquindio.quindioflix.business.model.RolUsuario;
import co.edu.uniquindio.quindioflix.business.service.ReporteContenidoService;
import co.edu.uniquindio.quindioflix.persistence.entity.ContenidoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.ReporteContenidoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.UsuarioEntity;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.ContenidoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ReporteContenidoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteContenidoServiceImpl implements ReporteContenidoService {

    private final ReporteContenidoRepository reportes;
    private final UsuarioRepository usuarios;
    private final ContenidoRepository contenidos;

    @Override
    @Transactional
    public ReporteContenidoResponse crear(Long usuarioReportaId, CrearReporteContenidoCommand command) {
        UsuarioEntity usuario = usuarios.findById(usuarioReportaId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioReportaId));

        ContenidoEntity contenido = contenidos.findById(command.contenidoId())
                .orElseThrow(() -> new ResourceNotFoundException("Contenido", command.contenidoId()));

        ReporteContenidoEntity reporte = ReporteContenidoEntity.builder()
                .usuarioReporta(usuario)
                .contenido(contenido)
                .descripcion(command.descripcion().trim())
                .fechaReporte(LocalDateTime.now())
                .estado(EstadoReporte.PENDIENTE)
                .build();

        ReporteContenidoEntity guardado = reportes.save(reporte);
        log.info("Reporte de contenido creado: id={}, usuario={}, contenido={}", guardado.getId(), usuario.getId(), contenido.getId());
        return MapperService.reporte(guardado);
    }

    @Override
    @Transactional
    public ReporteContenidoResponse resolver(Long id, Long moderadorId, ResolverReporteContenidoCommand command) {
        ReporteContenidoEntity reporte = reportes.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reporte", id));

        UsuarioEntity moderador = usuarios.findById(moderadorId)
                .orElseThrow(() -> new ResourceNotFoundException("Moderador", moderadorId));

        validarModerador(moderador);
        validarReportePendiente(reporte);

        reporte.setModerador(moderador);
        reporte.setEstado(command.estado());
        reporte.setFechaResolucion(LocalDateTime.now());
        reporte.setComentarioResolucion(command.comentarioResolucion());

        log.info("Reporte de contenido resuelto: id={}, moderador={}, estado={}", id, moderador.getId(), command.estado());
        return MapperService.reporte(reporte);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReporteContenidoResponse> listar(EstadoReporte estado, Pageable pageable) {
        if (estado != null) {
            return reportes.findByEstadoOrderByFechaReporteDesc(estado, pageable)
                    .map(MapperService::reporte);
        }

        return reportes.findAllByOrderByFechaReporteDesc(pageable)
                .map(MapperService::reporte);
    }

    private void validarModerador(UsuarioEntity moderador) {
        if (moderador.getRol() != RolUsuario.MODERADOR && moderador.getRol() != RolUsuario.ADMIN) {
            throw new BusinessException("USER_NOT_MODERATOR", "Solo MODERADOR o ADMIN puede resolver reportes.");
        }
    }

    private void validarReportePendiente(ReporteContenidoEntity reporte) {
        if (reporte.getEstado() != EstadoReporte.PENDIENTE) {
            throw new BusinessException("REPORT_CLOSED", "El reporte ya fue cerrado.");
        }
    }
}
