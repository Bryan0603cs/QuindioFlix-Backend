package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarEpisodioCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearEpisodioCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.EpisodioResponse;
import co.edu.uniquindio.quindioflix.business.exception.BusinessException;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.service.EpisodioService;
import co.edu.uniquindio.quindioflix.persistence.entity.EpisodioEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.TemporadaEntity;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.EpisodioRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ReproduccionRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.TemporadaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EpisodioServiceImpl implements EpisodioService {

    private final TemporadaRepository temporadas;
    private final EpisodioRepository episodios;
    private final ReproduccionRepository reproducciones;

    @Override
    @Transactional(readOnly = true)
    public List<EpisodioResponse> listarPorTemporada(Long contenidoId, Long temporadaId) {
        validarTemporadaPerteneceAContenido(contenidoId, temporadaId);

        return episodios.findByTemporadaIdOrderByNumeroEpisodioAsc(temporadaId)
                .stream()
                .map(MapperService::episodio)
                .toList();
    }

    @Override
    @Transactional
    public EpisodioResponse crear(Long contenidoId, Long temporadaId, CrearEpisodioCommand command) {
        TemporadaEntity temporada = buscarTemporada(contenidoId, temporadaId);
        validarNumeroDuplicado(temporadaId, command.numeroEpisodio(), null);

        EpisodioEntity episodio = EpisodioEntity.builder()
                .temporada(temporada)
                .numeroEpisodio(command.numeroEpisodio())
                .titulo(command.titulo().trim())
                .duracionMinutos(command.duracionMinutos())
                .sinopsis(command.sinopsis())
                .build();

        EpisodioEntity guardado = episodios.save(episodio);
        log.info("Episodio creado: id={}, temporada={}, contenido={}", guardado.getId(), temporadaId, contenidoId);
        return MapperService.episodio(guardado);
    }

    @Override
    @Transactional
    public EpisodioResponse actualizar(
            Long contenidoId,
            Long temporadaId,
            Long episodioId,
            ActualizarEpisodioCommand command
    ) {
        validarTemporadaPerteneceAContenido(contenidoId, temporadaId);
        EpisodioEntity episodio = episodios.findById(episodioId)
                .orElseThrow(() -> new ResourceNotFoundException("Episodio", episodioId));

        if (!episodio.getTemporada().getId().equals(temporadaId)) {
            throw new BusinessException("EPISODE_NOT_IN_SEASON", "El episodio no pertenece a la temporada indicada.");
        }

        validarNumeroDuplicado(temporadaId, command.numeroEpisodio(), episodioId);

        episodio.setNumeroEpisodio(command.numeroEpisodio());
        episodio.setTitulo(command.titulo().trim());
        episodio.setDuracionMinutos(command.duracionMinutos());
        episodio.setSinopsis(command.sinopsis());

        log.info("Episodio actualizado: id={}, temporada={}, contenido={}", episodioId, temporadaId, contenidoId);
        return MapperService.episodio(episodio);
    }

    @Override
    @Transactional
    public void eliminar(Long contenidoId, Long temporadaId, Long episodioId) {
        validarTemporadaPerteneceAContenido(contenidoId, temporadaId);

        if (!episodios.existsByIdAndTemporadaId(episodioId, temporadaId)) {
            throw new ResourceNotFoundException("Episodio", episodioId);
        }

        reproducciones.deleteByEpisodioId(episodioId);
        episodios.deleteById(episodioId);
        log.info("Episodio eliminado: id={}, temporada={}, contenido={}", episodioId, temporadaId, contenidoId);
    }

    private TemporadaEntity buscarTemporada(Long contenidoId, Long temporadaId) {
        return temporadas.findByIdAndContenidoId(temporadaId, contenidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Temporada", temporadaId));
    }

    private void validarTemporadaPerteneceAContenido(Long contenidoId, Long temporadaId) {
        if (!temporadas.existsByIdAndContenidoId(temporadaId, contenidoId)) {
            throw new ResourceNotFoundException("Temporada del contenido " + contenidoId, temporadaId);
        }
    }

    private void validarNumeroDuplicado(Long temporadaId, Integer numeroEpisodio, Long episodioActualId) {
        boolean numeroDuplicado = episodios.findByTemporadaIdOrderByNumeroEpisodioAsc(temporadaId)
                .stream()
                .anyMatch(episodio -> episodio.getNumeroEpisodio().equals(numeroEpisodio)
                        && !episodio.getId().equals(episodioActualId));

        if (numeroDuplicado) {
            throw new BusinessException(
                    "DUPLICATED_EPISODE_NUMBER",
                    "Ya existe un episodio con ese número en la temporada."
            );
        }
    }
}
