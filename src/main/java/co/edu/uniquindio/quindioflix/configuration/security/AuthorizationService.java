package co.edu.uniquindio.quindioflix.configuration.security;

import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import co.edu.uniquindio.quindioflix.business.model.RolUsuario;
import co.edu.uniquindio.quindioflix.persistence.repository.CalificacionRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.PerfilRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ReproduccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("authorizationService")
@RequiredArgsConstructor
public class AuthorizationService {

    private final PerfilRepository perfiles;
    private final ReproduccionRepository reproducciones;
    private final CalificacionRepository calificaciones;

    public boolean canAccessUser(Long usuarioId) {
        AuthenticatedUser autenticado = currentUser();
        return activo(autenticado) && (isAdmin(autenticado) || autenticado.usuarioId().equals(usuarioId));
    }

    public boolean canAccessPerfil(Long perfilId) {
        AuthenticatedUser autenticado = currentUser();
        return activo(autenticado) && (isAdmin(autenticado) || perfiles.existsByIdAndUsuarioId(perfilId, autenticado.usuarioId()));
    }

    public boolean canAccessReproduccion(Long reproduccionId) {
        AuthenticatedUser autenticado = currentUser();
        return activo(autenticado) && (isAdmin(autenticado) || reproducciones.existsByIdAndPerfilUsuarioId(reproduccionId, autenticado.usuarioId()));
    }

    public boolean canAccessCalificacion(Long calificacionId) {
        AuthenticatedUser autenticado = currentUser();
        return activo(autenticado) && (isAdmin(autenticado) || calificaciones.existsByIdAndPerfilUsuarioId(calificacionId, autenticado.usuarioId()));
    }

    public boolean canModerate() {
        AuthenticatedUser autenticado = currentUser();
        return activo(autenticado) && (isAdmin(autenticado) || autenticado.rol() == RolUsuario.MODERADOR);
    }

    public Long currentUserId() {
        return currentUser().usuarioId();
    }

    private boolean isAdmin(AuthenticatedUser user) {
        return user.rol() == RolUsuario.ADMIN;
    }

    private boolean activo(AuthenticatedUser user) {
        return user.estadoCuenta() == EstadoCuenta.ACTIVO;
    }

    private AuthenticatedUser currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser user)) {
            throw new IllegalStateException("No hay usuario autenticado válido en el contexto de seguridad.");
        }

        return user;
    }
}
