package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.CambiarPlanCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarUsuarioCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.UsuarioResponse;

import java.util.List;

public interface UsuarioService {

    UsuarioResponse registrar(RegistrarUsuarioCommand command);

    UsuarioResponse cambiarPlan(Long usuarioId, CambiarPlanCommand command);

    UsuarioResponse buscar(Long id);

    List<ContenidoResponse> favoritosDeUsuario(Long usuarioId);

    List<UsuarioResponse> referidosActivos(Long usuarioId);

    UsuarioResponse referenteDeUsuario(Long usuarioId);

    void eliminarCuenta(Long usuarioId);
}
