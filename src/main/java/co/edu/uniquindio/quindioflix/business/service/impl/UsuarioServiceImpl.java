package co.edu.uniquindio.quindioflix.business.service.impl;

import co.edu.uniquindio.quindioflix.business.dto.command.CambiarPlanCommand;
import co.edu.uniquindio.quindioflix.business.dto.command.RegistrarUsuarioCommand;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.PagoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.UsuarioResponse;
import co.edu.uniquindio.quindioflix.business.exception.EmailAlreadyExistsException;
import co.edu.uniquindio.quindioflix.business.exception.MaxProfilesExceededException;
import co.edu.uniquindio.quindioflix.business.exception.ResourceNotFoundException;
import co.edu.uniquindio.quindioflix.business.model.EstadoCuenta;
import co.edu.uniquindio.quindioflix.business.model.EstadoPago;
import co.edu.uniquindio.quindioflix.business.model.RolUsuario;
import co.edu.uniquindio.quindioflix.business.model.TipoPerfil;
import co.edu.uniquindio.quindioflix.business.service.UsuarioService;
import co.edu.uniquindio.quindioflix.persistence.entity.CambioPlanEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.FavoritoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.PagoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.PerfilEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.PlanEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.UsuarioEntity;
import co.edu.uniquindio.quindioflix.persistence.mapper.MapperService;
import co.edu.uniquindio.quindioflix.persistence.repository.CalificacionRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.CambioPlanRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.FavoritoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.PagoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.PerfilRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.PlanRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ReporteContenidoRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.ReproduccionRepository;
import co.edu.uniquindio.quindioflix.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private static final BigDecimal REFERRED_DISCOUNT_RATE = new BigDecimal("0.10");

    private final UsuarioRepository usuarios;
    private final PlanRepository planes;
    private final PerfilRepository perfiles;
    private final PagoRepository pagos;
    private final FavoritoRepository favoritos;
    private final CalificacionRepository calificaciones;
    private final ReproduccionRepository reproducciones;
    private final ReporteContenidoRepository reportesContenido;
    private final CambioPlanRepository cambiosPlan;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public UsuarioResponse registrar(RegistrarUsuarioCommand command) {
        String email = command.email().trim().toLowerCase();

        if (usuarios.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        PlanEntity plan = planes.findById(command.planId())
                .filter(PlanEntity::activo)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", command.planId()));

        UsuarioEntity referidoPor = buscarReferente(command.referidoPorId());
        LocalDateTime now = LocalDateTime.now();

        UsuarioEntity usuario = UsuarioEntity.builder()
                .nombre(command.nombre().trim())
                .email(email)
                .telefono(command.telefono().trim())
                .fechaNacimiento(command.fechaNacimiento())
                .ciudad(command.ciudad().trim())
                .passwordHash(encoder.encode(command.password()))
                .plan(plan)
                .estadoCuenta(EstadoCuenta.ACTIVO)
                .fechaRegistro(now)
                .fechaUltimoPago(now)
                .fechaVencimiento(now.plusDays(30))
                .referidoPor(referidoPor)
                .rol(RolUsuario.CLIENTE)
                .build();

        usuario = usuarios.save(usuario);
        crearPerfilPrincipal(usuario);
        registrarPrimerPago(command, usuario, plan, referidoPor, now);
        log.info("Usuario registrado: id={}, email={}, plan={}", usuario.getId(), usuario.getEmail(), plan.getNombre());

        return MapperService.usuario(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponse cambiarPlan(Long usuarioId, CambiarPlanCommand command) {
        UsuarioEntity usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioId));

        PlanEntity nuevoPlan = planes.findById(command.nuevoPlanId())
                .filter(PlanEntity::activo)
                .orElseThrow(() -> new ResourceNotFoundException("Plan", command.nuevoPlanId()));

        long totalPerfiles = perfiles.countByUsuarioId(usuarioId);
        if (totalPerfiles > nuevoPlan.getMaxPerfiles()) {
            throw new MaxProfilesExceededException(nuevoPlan.getMaxPerfiles());
        }

        PlanEntity planAnterior = usuario.getPlan();
        usuario.setPlan(nuevoPlan);

        cambiosPlan.save(CambioPlanEntity.builder()
                .usuario(usuario)
                .planAnterior(planAnterior)
                .planNuevo(nuevoPlan)
                .fechaCambio(LocalDateTime.now())
                .motivo(command.motivo())
                .build());

        log.info("Plan cambiado: usuario={}, planAnterior={}, planNuevo={}", usuarioId, planAnterior.getId(), nuevoPlan.getId());
        return MapperService.usuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse buscar(Long id) {
        return usuarios.findById(id)
                .map(MapperService::usuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContenidoResponse> favoritosDeUsuario(Long usuarioId) {
        if (!usuarios.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario", usuarioId);
        }

        Map<Long, ContenidoResponse> contenidosUnicos = new LinkedHashMap<>();

        favoritos.findByPerfilUsuarioIdOrderByFechaAgregadoDesc(usuarioId)
                .stream()
                .map(FavoritoEntity::getContenido)
                .map(MapperService::contenido)
                .forEach(contenido -> contenidosUnicos.putIfAbsent(contenido.id(), contenido));

        return contenidosUnicos.values().stream().toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> referidosActivos(Long usuarioId) {
        if (!usuarios.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario", usuarioId);
        }

        return usuarios.findByReferidoPorIdOrderByFechaRegistroDesc(usuarioId)
                .stream()
                .filter(UsuarioEntity::activo)
                .map(MapperService::usuario)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse referenteDeUsuario(Long usuarioId) {
        UsuarioEntity usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioId));

        if (usuario.getReferidoPor() == null) {
            return null;
        }

        return MapperService.usuario(usuario.getReferidoPor());
    }

    @Override
    @Transactional
    public void eliminarCuenta(Long usuarioId) {
        if (!usuarios.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario", usuarioId);
        }

        calificaciones.deleteByUsuarioId(usuarioId);
        favoritos.deleteByUsuarioId(usuarioId);
        reproducciones.deleteByUsuarioId(usuarioId);
        reportesContenido.clearModerador(usuarioId);
        reportesContenido.deleteByUsuarioReportaId(usuarioId);
        perfiles.deleteByUsuarioId(usuarioId);
        pagos.deleteByUsuarioId(usuarioId);
        cambiosPlan.deleteByUsuarioId(usuarioId);
        usuarios.clearReferenciasReferidoPor(usuarioId);
        usuarios.deleteById(usuarioId);
        log.info("Cuenta eliminada: usuario={}", usuarioId);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listar(RolUsuario rol, EstadoCuenta estado, Long planId, String ciudad, Pageable pageable) {
        return usuarios.buscarUsuarios(rol, estado, planId, ciudad, pageable)
                .map(MapperService::usuario);
    }

    @Override
    @Transactional
    public UsuarioResponse cambiarEstado(Long usuarioId, EstadoCuenta nuevoEstado) {
        UsuarioEntity usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioId));

        usuario.setEstadoCuenta(nuevoEstado);
        log.warn("Estado de cuenta actualizado: usuario={}, estado={}", usuarioId, nuevoEstado);
        return MapperService.usuario(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponse cambiarRol(Long usuarioId, RolUsuario nuevoRol) {
        UsuarioEntity usuario = usuarios.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", usuarioId));

        usuario.setRol(nuevoRol);
        log.warn("Rol actualizado: usuario={}, rol={}", usuarioId, nuevoRol);
        return MapperService.usuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PagoResponse> pagosDeUsuario(Long usuarioId, Pageable pageable) {
        if (!usuarios.existsById(usuarioId)) {
            throw new ResourceNotFoundException("Usuario", usuarioId);
        }
        return pagos.findByUsuarioIdOrderByFechaPagoDesc(usuarioId, pageable)
                .map(MapperService::pago);
    }

    private UsuarioEntity buscarReferente(Long referidoPorId) {
        if (referidoPorId == null) {
            return null;
        }

        return usuarios.findById(referidoPorId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario referente", referidoPorId));
    }

    private void crearPerfilPrincipal(UsuarioEntity usuario) {
        perfiles.save(PerfilEntity.builder()
                .usuario(usuario)
                .nombre("Principal")
                .avatar("avatar-default.png")
                .tipoPerfil(TipoPerfil.ADULTO)
                .build());
    }

    private void registrarPrimerPago(
            RegistrarUsuarioCommand command,
            UsuarioEntity usuario,
            PlanEntity plan,
            UsuarioEntity referidoPor,
            LocalDateTime fechaPago
    ) {
        BigDecimal descuento = calcularDescuentoInicial(plan, referidoPor);
        BigDecimal montoFinal = plan.getPrecioMensual()
                .subtract(descuento)
                .setScale(2, RoundingMode.HALF_UP);

        pagos.save(PagoEntity.builder()
                .usuario(usuario)
                .fechaPago(fechaPago)
                .monto(montoFinal)
                .metodoPago(command.metodoPagoPrimerPago())
                .estadoPago(EstadoPago.EXITOSO)
                .referencia("PAGO-INICIAL")
                .descuentoAplicado(descuento)
                .build());
    }

    private BigDecimal calcularDescuentoInicial(PlanEntity plan, UsuarioEntity referidoPor) {
        if (referidoPor == null || !referidoPor.activo()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return plan.getPrecioMensual()
                .multiply(REFERRED_DISCOUNT_RATE)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
