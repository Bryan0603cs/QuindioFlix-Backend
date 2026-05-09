package co.edu.uniquindio.quindioflix.configuration.security;

import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import co.edu.uniquindio.quindioflix.business.model.RolUsuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public record AuthenticatedUser(
        Long usuarioId,
        String email,
        String password,
        RolUsuario rol,
        EstadoCuenta estadoCuenta,
        Collection<? extends GrantedAuthority> authorities
) implements UserDetails {

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return estadoCuenta == EstadoCuenta.ACTIVO;
    }

    @Override
    public boolean isAccountNonLocked() {
        return estadoCuenta == EstadoCuenta.ACTIVO;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return estadoCuenta == EstadoCuenta.ACTIVO;
    }
}
