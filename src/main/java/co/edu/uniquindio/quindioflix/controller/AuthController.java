package co.edu.uniquindio.quindioflix.controller;

import co.edu.uniquindio.quindioflix.business.dto.command.LoginCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarUsuarioCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.AuthResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.UsuarioResponse;
import co.edu.uniquindio.quindioflix.business.service.AuthService;
import co.edu.uniquindio.quindioflix.business.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Autenticación")
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Registrar usuario", description = "Crea una cuenta de cliente con perfil principal y primer pago calculado desde el plan.")
    public UsuarioResponse register(@Valid @RequestBody RegistrarUsuarioCommand command) {
        return usuarioService.registrar(command);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica al usuario y devuelve el token JWT para consumir endpoints protegidos.")
    public AuthResponse login(@Valid @RequestBody LoginCommand command) {
        return authService.login(command);
    }
}
