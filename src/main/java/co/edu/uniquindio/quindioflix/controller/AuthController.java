package co.edu.uniquindio.quindioflix.controller;

import io.swagger.v3.oas.annotations.Operation;
import co.edu.uniquindio.quindioflix.business.dto.command.LoginCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarUsuarioCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.AuthResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.UsuarioResponse;
import co.edu.uniquindio.quindioflix.business.service.AuthService;
import co.edu.uniquindio.quindioflix.business.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar usuario")
    public UsuarioResponse register(@Valid @RequestBody RegistrarUsuarioCommand command) {
        return usuarioService.registrar(command);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión")
    public AuthResponse login(@Valid @RequestBody LoginCommand command) {
        return authService.login(command);
    }
}
