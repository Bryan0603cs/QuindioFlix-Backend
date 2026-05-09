package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.command.AgregarFavoritoCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;
import co.edu.uniquindio.quindioflix.business.exception.BusinessException;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.service.FavoritoService;
import co.edu.uniquindio.quindioflix.persistence.entity.ContenidoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.FavoritoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.PerfilEntity;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.ContenidoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.FavoritoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.PerfilRepository;
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
public class FavoritoServiceImpl implements FavoritoService {

    private final FavoritoRepository favoritos;
    private final PerfilRepository perfiles;
    private final ContenidoRepository contenidos;

    @Override
    @Transactional
    public Long agregar(AgregarFavoritoCommand command) {
        PerfilEntity perfil = perfiles.findById(command.perfilId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", command.perfilId()));

        ContenidoEntity contenido = contenidos.findById(command.contenidoId())
                .orElseThrow(() -> new ResourceNotFoundException("Contenido", command.contenidoId()));

        if (favoritos.existsByPerfilIdAndContenidoId(command.perfilId(), command.contenidoId())) {
            throw new BusinessException("DUPLICATED_FAVORITE", "El contenido ya está en favoritos.");
        }

        FavoritoEntity favorito = FavoritoEntity.builder()
                .perfil(perfil)
                .contenido(contenido)
                .fechaAgregado(LocalDateTime.now())
                .build();

        FavoritoEntity guardado = favoritos.save(favorito);
        log.info("Favorito agregado: id={}, perfil={}, contenido={}", guardado.getId(), perfil.getId(), contenido.getId());
        return guardado.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ContenidoResponse> listarPorPerfil(Long perfilId, Pageable pageable) {
        if (!perfiles.existsById(perfilId)) {
            throw new ResourceNotFoundException("Perfil", perfilId);
        }

        return favoritos.findByPerfilIdOrderByFechaAgregadoDesc(perfilId, pageable)
                .map(FavoritoEntity::getContenido)
                .map(MapperService::contenido);
    }

    @Override
    @Transactional
    public void eliminar(Long perfilId, Long contenidoId) {
        FavoritoEntity favorito = favoritos.findByPerfilIdAndContenidoId(perfilId, contenidoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Favorito del perfil " + perfilId,
                        contenidoId
                ));

        favoritos.delete(favorito);
        log.info("Favorito eliminado: perfil={}, contenido={}", perfilId, contenidoId);
    }
}
