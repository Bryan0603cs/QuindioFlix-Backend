package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarAvanceReproduccionCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarReproduccionCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ReproduccionResponse;
import co.edu.uniquindio.quindioflix.business.exception.BusinessException;
import co.edu.uniquindio.quindioflix.business.exception.InactiveAccountException;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.service.ReproduccionService;
import co.edu.uniquindio.quindioflix.persistence.entity.ContenidoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.EpisodioEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.PerfilEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.ReproduccionEntity;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.ContenidoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.EpisodioRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.PerfilRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ReproduccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReproduccionServiceImpl implements ReproduccionService {

    private final ReproduccionRepository reproducciones;
    private final PerfilRepository perfiles;
    private final ContenidoRepository contenidos;
    private final EpisodioRepository episodios;

    @Override
    @Transactional
    public ReproduccionResponse registrar(RegistrarReproduccionCommand command) {
        PerfilEntity perfil = perfiles.findById(command.perfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", command.perfilId()));

        if (!perfil.getUsuario().activo()) {
            throw new InactiveAccountException(perfil.getUsuario().getId());
        }

        ContenidoEntity contenido = contenidos.findById(command.contenidoId())
                .orElseThrow(() -> new ResourceNotFoundException("Contenido", command.contenidoId()));

        validarAccesoInfantil(perfil, contenido);
        validarFechas(command.fechaHoraInicio(), command.fechaHoraFin());

        EpisodioEntity episodio = buscarEpisodio(command.episodioId(), contenido.getId());

        ReproduccionEntity reproduccion = ReproduccionEntity.builder()
                .perfil(perfil)
                .contenido(contenido)
                .episodio(episodio)
                .fechaHoraInicio(command.fechaHoraInicio())
                .fechaHoraFin(command.fechaHoraFin())
                .dispositivo(command.dispositivo())
                .porcentajeAvance(command.porcentajeAvance())
                .build();

        ReproduccionEntity guardada = reproducciones.save(reproduccion);
        recalcularPopularidadContenido(contenido.getId());
        log.info("Reproducción registrada: id={}, perfil={}, contenido={}, avance={}", guardada.getId(), perfil.getId(), contenido.getId(), guardada.getPorcentajeAvance());
        return MapperService.reproduccion(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReproduccionResponse> listarPorPerfil(Long perfilId, Pageable pageable) {
        if (!perfiles.existsById(perfilId)) {
            throw new ResourceNotFoundException("Perfil", perfilId);
        }

        return reproducciones.findByPerfilIdOrderByFechaHoraInicioDesc(perfilId, pageable)
                .map(MapperService::reproduccion);
    }

    @Override
    @Transactional
    public ReproduccionResponse actualizarAvance(Long reproduccionId, ActualizarAvanceReproduccionCommand command) {
        ReproduccionEntity reproduccion = reproducciones.findById(reproduccionId)
                .orElseThrow(() -> new ResourceNotFoundException("Reproducción", reproduccionId));

        validarFechas(reproduccion.getFechaHoraInicio(), command.fechaHoraFin());

        reproduccion.setPorcentajeAvance(command.porcentajeAvance());
        if (command.fechaHoraFin() != null) {
            reproduccion.setFechaHoraFin(command.fechaHoraFin());
        }

        recalcularPopularidadContenido(reproduccion.getContenido().getId());
        log.info("Avance de reproducción actualizado: id={}, avance={}", reproduccionId, command.porcentajeAvance());
        return MapperService.reproduccion(reproduccion);
    }

    private void recalcularPopularidadContenido(Long contenidoId) {
        contenidos.findById(contenidoId).ifPresent(contenido -> {
            long totalCompletas = reproducciones.countByContenidoIdAndPorcentajeAvanceGreaterThanEqual(contenidoId, 90);
            contenido.setPopularidad(Math.toIntExact(totalCompletas));
        });
    }

    private void validarAccesoInfantil(PerfilEntity perfil, ContenidoEntity contenido) {
        if (perfil.infantil() && !contenido.getClasificacionEdad().permitidoInfantil()) {
            throw new BusinessException(
                    "CHILD_PROFILE_RESTRICTION",
                    "El perfil infantil solo puede reproducir contenido TP, +7 o +13."
            );
        }
    }

    private void validarFechas(java.time.LocalDateTime inicio, java.time.LocalDateTime fin) {
        if (fin != null && fin.isBefore(inicio)) {
            throw new BusinessException("INVALID_DATES", "La fecha fin no puede ser anterior a la fecha de inicio.");
        }
    }

    private EpisodioEntity buscarEpisodio(Long episodioId, Long contenidoId) {
        if (episodioId == null) {
            return null;
        }

        EpisodioEntity episodio = episodios.findById(episodioId)
                .orElseThrow(() -> new ResourceNotFoundException("Episodio", episodioId));

        if (!episodio.getTemporada().getContenido().getId().equals(contenidoId)) {
            throw new BusinessException(
                    "EPISODE_CONTENT_MISMATCH",
                    "El episodio no pertenece al contenido indicado."
            );
        }

        return episodio;
    }
}
