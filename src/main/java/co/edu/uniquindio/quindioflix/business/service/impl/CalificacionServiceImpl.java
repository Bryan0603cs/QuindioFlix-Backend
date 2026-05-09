package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarCalificacionCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CalificarContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.CalificacionResponse;
import co.edu.uniquindio.quindioflix.business.exception.BusinessException;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.service.CalificacionService;
import co.edu.uniquindio.quindioflix.persistence.entity.CalificacionEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.ContenidoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.PerfilEntity;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.CalificacionRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ContenidoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.PerfilRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ReproduccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalificacionServiceImpl implements CalificacionService {

    private static final int MINIMUM_PROGRESS_TO_RATE = 50;

    private final CalificacionRepository calificaciones;
    private final ReproduccionRepository reproducciones;
    private final PerfilRepository perfiles;
    private final ContenidoRepository contenidos;

    @Override
    @Transactional
    public CalificacionResponse calificar(CalificarContenidoCommand command) {
        PerfilEntity perfil = perfiles.findById(command.perfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", command.perfilId()));

        ContenidoEntity contenido = contenidos.findById(command.contenidoId())
                .orElseThrow(() -> new ResourceNotFoundException("Contenido", command.contenidoId()));

        validarAccesoInfantil(perfil, contenido);
        validarReproduccionPrevia(command.perfilId(), command.contenidoId());
        validarCalificacionDuplicada(command);

        CalificacionEntity calificacion = CalificacionEntity.builder()
                .perfil(perfil)
                .contenido(contenido)
                .estrellas(command.estrellas())
                .resena(command.resena())
                .fechaCalificacion(LocalDateTime.now())
                .build();

        CalificacionEntity guardada = calificaciones.save(calificacion);
        log.info("Calificación creada: id={}, perfil={}, contenido={}", guardada.getId(), perfil.getId(), contenido.getId());
        return MapperService.calificacion(guardada);
    }

    @Override
    @Transactional
    public CalificacionResponse actualizar(Long calificacionId, ActualizarCalificacionCommand command) {
        CalificacionEntity calificacion = calificaciones.findById(calificacionId)
                .orElseThrow(() -> new ResourceNotFoundException("Calificación", calificacionId));

        validarReproduccionPrevia(calificacion.getPerfil().getId(), calificacion.getContenido().getId());
        calificacion.setEstrellas(command.estrellas());
        calificacion.setResena(command.resena());
        calificacion.setFechaCalificacion(LocalDateTime.now());

        log.info("Calificación actualizada: id={}", calificacionId);
        return MapperService.calificacion(calificacion);
    }

    @Override
    @Transactional
    public void eliminar(Long calificacionId) {
        if (!calificaciones.existsById(calificacionId)) {
            throw new ResourceNotFoundException("Calificación", calificacionId);
        }

        calificaciones.deleteById(calificacionId);
        log.info("Calificación eliminada: id={}", calificacionId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalificacionResponse> listarPorContenido(Long contenidoId) {
        if (!contenidos.existsById(contenidoId)) {
            throw new ResourceNotFoundException("Contenido", contenidoId);
        }

        return calificaciones.findByContenidoIdOrderByFechaCalificacionDesc(contenidoId)
                .stream()
                .map(MapperService::calificacion)
                .toList();
    }

    private void validarAccesoInfantil(PerfilEntity perfil, ContenidoEntity contenido) {
        if (perfil.infantil() && !contenido.getClasificacionEdad().permitidoInfantil()) {
            throw new BusinessException(
                    "CHILD_PROFILE_RESTRICTION",
                    "El perfil infantil solo puede calificar contenido TP, +7 o +13."
            );
        }
    }

    private void validarReproduccionPrevia(Long perfilId, Long contenidoId) {
        boolean puedeCalificar = reproducciones.existeConAvanceMinimo(
                perfilId,
                contenidoId,
                MINIMUM_PROGRESS_TO_RATE
        );

        if (!puedeCalificar) {
            throw new BusinessException(
                    "RATING_NOT_ALLOWED",
                    "El perfil debe reproducir al menos el 50% del contenido antes de calificarlo."
            );
        }
    }

    private void validarCalificacionDuplicada(CalificarContenidoCommand command) {
        if (calificaciones.existsByPerfilIdAndContenidoId(command.perfilId(), command.contenidoId())) {
            throw new BusinessException("DUPLICATED_RATING", "El perfil ya calificó este contenido.");
        }
    }
}
