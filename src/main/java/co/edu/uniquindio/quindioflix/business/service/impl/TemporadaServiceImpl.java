package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarTemporadaCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearTemporadaCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.TemporadaResponse;
import co.edu.uniquindio.quindioflix.business.exception.BusinessException;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.service.TemporadaService;
import co.edu.uniquindio.quindioflix.business.model.TipoContenido;
import co.edu.uniquindio.quindioflix.persistence.entity.ContenidoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.EpisodioEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.TemporadaEntity;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.ContenidoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.EpisodioRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ReproduccionRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.TemporadaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemporadaServiceImpl implements TemporadaService {

    private final ContenidoRepository contenidos;
    private final TemporadaRepository temporadas;
    private final EpisodioRepository episodios;
    private final ReproduccionRepository reproducciones;

    @Override
    @Transactional(readOnly = true)
    public List<TemporadaResponse> listarPorContenido(Long contenidoId) {
        if (!contenidos.existsById(contenidoId)) {
            throw new ResourceNotFoundException("Contenido", contenidoId);
        }

        List<TemporadaEntity> temporadasContenido = temporadas.findByContenidoIdOrderByNumeroTemporadaAsc(contenidoId);
        List<Long> temporadaIds = temporadasContenido.stream()
                .map(TemporadaEntity::getId)
                .toList();

        if (temporadaIds.isEmpty()) {
            return List.of();
        }

        Map<Long, List<EpisodioEntity>> episodiosPorTemporada = episodios
                .findByTemporadaIds(temporadaIds)
                .stream()
                .collect(Collectors.groupingBy(episodio -> episodio.getTemporada().getId()));

        return temporadasContenido.stream()
                .map(temporada -> MapperService.temporada(
                        temporada,
                        episodiosPorTemporada.getOrDefault(temporada.getId(), List.of())
                ))
                .toList();
    }

    @Override
    @Transactional
    public TemporadaResponse crear(Long contenidoId, CrearTemporadaCommand command) {
        ContenidoEntity contenido = contenidos.findById(contenidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contenido", contenidoId));
        validarContenidoConTemporadas(contenido);
        validarNumeroDuplicado(contenidoId, command.numeroTemporada(), null);

        TemporadaEntity temporada = TemporadaEntity.builder()
                .contenido(contenido)
                .numeroTemporada(command.numeroTemporada())
                .titulo(command.titulo().trim())
                .fechaLanzamiento(command.fechaLanzamiento())
                .build();

        TemporadaEntity guardada = temporadas.save(temporada);
        log.info("Temporada creada: id={}, contenido={}", guardada.getId(), contenidoId);
        return MapperService.temporada(guardada, List.of());
    }

    @Override
    @Transactional
    public TemporadaResponse actualizar(Long contenidoId, Long temporadaId, ActualizarTemporadaCommand command) {
        TemporadaEntity temporada = temporadas.findByIdAndContenidoId(temporadaId, contenidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Temporada", temporadaId));
        validarNumeroDuplicado(contenidoId, command.numeroTemporada(), temporadaId);

        temporada.setNumeroTemporada(command.numeroTemporada());
        temporada.setTitulo(command.titulo().trim());
        temporada.setFechaLanzamiento(command.fechaLanzamiento());

        List<EpisodioEntity> episodiosTemporada = episodios.findByTemporadaIdOrderByNumeroEpisodioAsc(temporadaId);
        log.info("Temporada actualizada: id={}, contenido={}", temporadaId, contenidoId);
        return MapperService.temporada(temporada, episodiosTemporada);
    }

    @Override
    @Transactional
    public void eliminar(Long contenidoId, Long temporadaId) {
        if (!temporadas.existsByIdAndContenidoId(temporadaId, contenidoId)) {
            throw new ResourceNotFoundException("Temporada", temporadaId);
        }

        reproducciones.deleteByTemporadaId(temporadaId);
        episodios.deleteByTemporadaId(temporadaId);
        temporadas.deleteById(temporadaId);
        log.info("Temporada eliminada: id={}, contenido={}", temporadaId, contenidoId);
    }

    private void validarContenidoConTemporadas(ContenidoEntity contenido) {
        TipoContenido tipo = contenido.getCategoria().getTipoContenido();
        if (tipo != TipoContenido.SERIE && tipo != TipoContenido.PODCAST) {
            throw new BusinessException(
                    "INVALID_SEASON_CONTENT_TYPE",
                    "Solo los contenidos de tipo SERIE o PODCAST pueden tener temporadas."
            );
        }
    }

    private void validarNumeroDuplicado(Long contenidoId, Integer numeroTemporada, Long temporadaActualId) {
        boolean numeroDuplicado = temporadas.findByContenidoIdOrderByNumeroTemporadaAsc(contenidoId)
                .stream()
                .anyMatch(temporada -> temporada.getNumeroTemporada().equals(numeroTemporada)
                        && !temporada.getId().equals(temporadaActualId));

        if (numeroDuplicado) {
            throw new BusinessException(
                    "DUPLICATED_SEASON_NUMBER",
                    "Ya existe una temporada con ese número para el contenido."
            );
        }
    }
}
