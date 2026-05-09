package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.command.CrearPerfilCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.EditarPerfilCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.PerfilResponse;
import co.edu.uniquindio.quindioflix.business.exception.MaxProfilesExceededException;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.service.PerfilService;
import co.edu.uniquindio.quindioflix.persistence.entity.PerfilEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.UsuarioEntity;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.PerfilRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements PerfilService {

    private final PerfilRepository perfiles;
    private final UsuarioRepository usuarios;

    @Override
    @Transactional
    public PerfilResponse crear(Long usuarioId, CrearPerfilCommand command) {
        UsuarioEntity usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioId));

        if (perfiles.countByUsuarioId(usuarioId) >= usuario.getPlan().getMaxPerfiles()) {
            throw new MaxProfilesExceededException(usuario.getPlan().getMaxPerfiles());
        }

        PerfilEntity perfil = PerfilEntity.builder()
                .usuario(usuario)
                .nombre(command.nombre().trim())
                .avatar(command.avatar())
                .tipoPerfil(command.tipoPerfil())
                .build();

        PerfilEntity guardado = perfiles.save(perfil);
        log.info("Perfil creado: id={}, usuario={}", guardado.getId(), usuarioId);
        return MapperService.perfil(guardado);
    }

    @Override
    @Transactional
    public PerfilResponse actualizar(Long perfilId, EditarPerfilCommand command) {
        PerfilEntity perfil = perfiles.findById(perfilId)
                .orElseThrow(() -> new ResourceNotFoundException("Perfil", perfilId));

        perfil.setNombre(command.nombre().trim());
        perfil.setAvatar(command.avatar());

        log.info("Perfil actualizado: id={}", perfilId);
        return MapperService.perfil(perfil);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PerfilResponse> listar(Long usuarioId) {
        if (!usuarios.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario", usuarioId);
        }

        return perfiles.findByUsuarioId(usuarioId).stream()
                .map(MapperService::perfil)
                .toList();
    }
}
