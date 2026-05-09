package co.edu.uniquindio.quindioflix.business.service;

import co.edu.uniquindio.quindioflix.business.dto.command.CambiarPlanCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarUsuarioCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.PagoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.UsuarioResponse;
import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import co.edu.uniquindio.quindioflix.business.model.RolUsuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {

    UsuarioResponse registrar(RegistrarUsuarioCommand command);

    UsuarioResponse cambiarPlan(Long usuarioId, CambiarPlanCommand command);

    UsuarioResponse buscar(Long id);

    Page<UsuarioResponse> listar(RolUsuario rol, EstadoCuenta estado, Long planId, String ciudad, Pageable pageable);

    UsuarioResponse cambiarEstado(Long usuarioId, EstadoCuenta nuevoEstado);

    UsuarioResponse cambiarRol(Long usuarioId, RolUsuario nuevoRol);

    Page<PagoResponse> pagosDeUsuario(Long usuarioId, Pageable pageable);

    List<ContenidoResponse> favoritosDeUsuario(Long usuarioId);

    List<UsuarioResponse> referidosActivos(Long usuarioId);

    UsuarioResponse referenteDeUsuario(Long usuarioId);

    void eliminarCuenta(Long usuarioId);
}
