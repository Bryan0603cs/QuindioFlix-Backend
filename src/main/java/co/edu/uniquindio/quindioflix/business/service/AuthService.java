package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.LoginCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginCommand command);
}
