package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.command.ActualizarContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.CrearRelacionContenidoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoRelacionadoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;
import co.edu.uniquindio.quindioflix.business.exception.BusinessException;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.service.ContenidoService;
import co.edu.uniquindio.quindioflix.persistence.entity.CategoriaEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.ContenidoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.ContenidoRelacionadoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.EmpleadoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.GeneroEntity;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.CalificacionRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.CategoriaRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ContenidoRelacionadoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ContenidoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.EmpleadoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.EpisodioRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.FavoritoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.GeneroRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ReporteContenidoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ReproduccionRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.TemporadaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContenidoServiceImpl implements ContenidoService {

    private final ContenidoRepository contenidos;
    private final ContenidoRelacionadoRepository relaciones;
    private final CategoriaRepository categorias;
    private final GeneroRepository generos;
    private final EmpleadoRepository empleados;
    private final ReproduccionRepository reproducciones;
    private final CalificacionRepository calificaciones;
    private final FavoritoRepository favoritos;
    private final ReporteContenidoRepository reportes;
    private final EpisodioRepository episodios;
    private final TemporadaRepository temporadas;

    @Override
    @Transactional(readOnly = true)
    public Page<ContenidoResponse> listar(String titulo, Long categoriaId, Long generoId, Pageable pageable) {
        String tituloNormalizado = titulo == null || titulo.isBlank() ? null : "%" + titulo.trim() + "%";

        return contenidos.buscarCatalogo(tituloNormalizado, categoriaId, generoId, pageable)
                .map(MapperService::contenido);
    }

    @Override
    @Transactional(readOnly = true)
    public ContenidoResponse buscar(Long id) {
        return contenidos.findById(id)
                .map(MapperService::contenido)
                .orElseThrow(() -> new ResourceNotFoundException("Contenido", id));
    }

    @Override
    @Transactional
    public ContenidoResponse crear(CrearContenidoCommand command) {
        CategoriaEntity categoria = buscarCategoria(command.categoriaId());
        EmpleadoEntity empleado = buscarEmpleado(command.empleadoResponsableId());
        Set<GeneroEntity> generosEncontrados = buscarGeneros(command.generoIds());

        ContenidoEntity contenido = ContenidoEntity.builder()
                .categoria(categoria)
                .titulo(command.titulo().trim())
                .anioLanzamiento(command.anioLanzamiento())
                .duracionMinutos(command.duracionMinutos())
                .sinopsis(command.sinopsis())
                .clasificacionEdad(command.clasificacionEdad())
                .fechaAgregado(LocalDate.now())
                .originalQuindioflix(command.originalQuindioflix() ? "S" : "N")
                .empleadoResponsable(empleado)
                .popularidad(0)
                .generos(generosEncontrados)
                .build();

        ContenidoEntity guardado = contenidos.save(contenido);
        log.info("Contenido creado: id={}, titulo={}", guardado.getId(), guardado.getTitulo());
        return MapperService.contenido(guardado);
    }

    @Override
    @Transactional
    public ContenidoResponse actualizar(Long id, ActualizarContenidoCommand command) {
        ContenidoEntity contenido = contenidos.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contenido", id));

        contenido.setCategoria(buscarCategoria(command.categoriaId()));
        contenido.setEmpleadoResponsable(buscarEmpleado(command.empleadoResponsableId()));
        contenido.setGeneros(buscarGeneros(command.generoIds()));
        contenido.setTitulo(command.titulo().trim());
        contenido.setAnioLanzamiento(command.anioLanzamiento());
        contenido.setDuracionMinutos(command.duracionMinutos());
        contenido.setSinopsis(command.sinopsis());
        contenido.setClasificacionEdad(command.clasificacionEdad());
        contenido.setOriginalQuindioflix(command.originalQuindioflix() ? "S" : "N");

        log.info("Contenido actualizado: id={}, titulo={}", contenido.getId(), contenido.getTitulo());
        return MapperService.contenido(contenido);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!contenidos.existsById(id)) {
            throw new ResourceNotFoundException("Contenido", id);
        }

        reproducciones.deleteByContenidoId(id);
        calificaciones.deleteByContenidoId(id);
        favoritos.deleteByContenidoId(id);
        reportes.deleteByContenidoId(id);
        relaciones.deleteByContenidoId(id);
        episodios.deleteByContenidoId(id);
        temporadas.deleteByContenidoId(id);
        contenidos.deleteGenerosByContenidoId(id);
        contenidos.deleteById(id);
        log.info("Contenido eliminado: id={}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContenidoRelacionadoResponse> relacionados(Long contenidoId) {
        if (!contenidos.existsById(contenidoId)) {
            throw new ResourceNotFoundException("Contenido", contenidoId);
        }

        return relaciones.findByOrigenId(contenidoId)
                .stream()
                .map(MapperService::contenidoRelacionado)
                .toList();
    }

    @Override
    @Transactional
    public ContenidoRelacionadoResponse agregarRelacionado(Long contenidoId, CrearRelacionContenidoCommand command) {
        if (contenidoId.equals(command.contenidoDestinoId())) {
            throw new BusinessException("INVALID_CONTENT_RELATION", "Un contenido no puede relacionarse consigo mismo.");
        }

        ContenidoEntity origen = contenidos.findById(contenidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contenido origen", contenidoId));
        ContenidoEntity destino = contenidos.findById(command.contenidoDestinoId())
                .orElseThrow(() -> new ResourceNotFoundException("Contenido destino", command.contenidoDestinoId()));

        String tipoRelacion = command.tipoRelacion().trim().toUpperCase();
        if (relaciones.existsByOrigenIdAndDestinoIdAndTipoRelacionIgnoreCase(contenidoId, destino.getId(), tipoRelacion)) {
            throw new BusinessException("DUPLICATED_CONTENT_RELATION", "La relación entre contenidos ya existe.");
        }

        ContenidoRelacionadoEntity relacion = ContenidoRelacionadoEntity.builder()
                .origen(origen)
                .destino(destino)
                .tipoRelacion(tipoRelacion)
                .descripcion(command.descripcion())
                .build();

        ContenidoRelacionadoEntity guardada = relaciones.save(relacion);
        log.info("Relación de contenido creada: origen={}, destino={}, tipo={}", contenidoId, destino.getId(), tipoRelacion);
        return MapperService.contenidoRelacionado(guardada);
    }

    private CategoriaEntity buscarCategoria(Long categoriaId) {
        return categorias.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoría", categoriaId));
    }

    private EmpleadoEntity buscarEmpleado(Long empleadoId) {
        return empleados.findById(empleadoId)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado", empleadoId));
    }

    private Set<GeneroEntity> buscarGeneros(List<Long> generoIds) {
        Set<GeneroEntity> generosEncontrados = new HashSet<>(generos.findAllById(generoIds));
        int generosSolicitados = new HashSet<>(generoIds).size();

        if (generosEncontrados.size() != generosSolicitados) {
            throw new BusinessException("GENRE_NOT_FOUND", "Uno o más géneros no existen.");
        }

        return generosEncontrados;
    }

    @Override
    @Transactional
    public int actualizarPopularidadCatalogo() {
        int actualizados = contenidos.recalcularPopularidadCatalogo();
        log.info("Popularidad del catálogo recalculada. Filas afectadas={}", actualizados);
        return actualizados;
    }

}