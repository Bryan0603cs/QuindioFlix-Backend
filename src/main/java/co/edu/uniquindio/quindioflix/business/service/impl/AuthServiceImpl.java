package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.command.LoginCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.AuthResponse;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.service.AuthService;
import co.edu.uniquindio.quindioflix.configuration.security.JwtService;
import co.edu.uniquindio.quindioflix.persistence.entity.UsuarioEntity;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarios;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginCommand command) {
        String email = command.email().trim().toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, command.password())
        );

        UsuarioEntity usuario = usuarios.findByEmailWithPlanAndReferente(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", email));

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtService.generate(
                userDetails,
                Map.of(
                        "usuarioId", usuario.getId(),
                        "rol", usuario.getRol().name(),
                        "estadoCuenta", usuario.getEstadoCuenta().name()
                )
        );

        log.info("Login exitoso usuarioId={} email={} rol={}", usuario.getId(), usuario.getEmail(), usuario.getRol());
        return new AuthResponse(token, MapperService.usuario(usuario));
    }
}
