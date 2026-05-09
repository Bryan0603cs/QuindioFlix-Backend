package co.edu.uniquindio.quindioflix.persistence.mapper;

import co.edu.uniquindio.quindioflix.business.dto.response.CalificacionResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.CategoriaResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoRelacionadoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.ContenidoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.EmpleadoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.EpisodioResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.GeneroResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.PagoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.PerfilResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.PlanResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.ReporteContenidoResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.TemporadaResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.ReproduccionResponse;
import co.edu.uniquindio.quindioflix.business.dto.response.UsuarioResponse;
import co.edu.uniquindio.quindioflix.persistence.entity.CalificacionEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.CategoriaEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.ContenidoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.ContenidoRelacionadoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.EmpleadoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.EpisodioEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.GeneroEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.PagoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.PerfilEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.PlanEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.ReporteContenidoEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.TemporadaEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.ReproduccionEntity;
import co.edu.uniquindio.quindioflix.persistence.entity.UsuarioEntity;

import java.util.List;

public final class MapperService {

    private MapperService() {
    }


    public static CategoriaResponse categoria(CategoriaEntity categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getTipoContenido()
        );
    }

    public static GeneroResponse genero(GeneroEntity genero) {
        return new GeneroResponse(
                genero.getId(),
                genero.getNombre()
        );
    }

    public static EpisodioResponse episodio(EpisodioEntity episodio) {
        return new EpisodioResponse(
                episodio.getId(),
                episodio.getTemporada().getId(),
                episodio.getNumeroEpisodio(),
                episodio.getTitulo(),
                episodio.getDuracionMinutos(),
                episodio.getSinopsis()
        );
    }

    public static TemporadaResponse temporada(TemporadaEntity temporada, List<EpisodioEntity> episodios) {
        return new TemporadaResponse(
                temporada.getId(),
                temporada.getContenido().getId(),
                temporada.getNumeroTemporada(),
                temporada.getTitulo(),
                temporada.getFechaLanzamiento(),
                episodios.stream().map(MapperService::episodio).toList()
        );
    }

    public static UsuarioResponse usuario(UsuarioEntity usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getFechaNacimiento(),
                usuario.getCiudad(),
                usuario.getPlan().getId(),
                usuario.getPlan().getNombre(),
                usuario.getEstadoCuenta(),
                usuario.getFechaRegistro(),
                usuario.getFechaUltimoPago(),
                usuario.getFechaVencimiento(),
                usuario.getReferidoPor() == null ? null : usuario.getReferidoPor().getId(),
                usuario.getRol()
        );
    }

    public static PlanResponse plan(PlanEntity plan) {
        return new PlanResponse(
                plan.getId(),
                plan.getNombre(),
                plan.getPantallasSimultaneas(),
                plan.getCalidad(),
                plan.getPrecioMensual(),
                plan.getMaxPerfiles()
        );
    }

    public static PerfilResponse perfil(PerfilEntity perfil) {
        return new PerfilResponse(
                perfil.getId(),
                perfil.getUsuario().getId(),
                perfil.getNombre(),
                perfil.getAvatar(),
                perfil.getTipoPerfil()
        );
    }

    public static ContenidoResponse contenido(ContenidoEntity contenido) {
        return new ContenidoResponse(
                contenido.getId(),
                contenido.getCategoria().getId(),
                contenido.getCategoria().getNombre(),
                contenido.getTitulo(),
                contenido.getAnioLanzamiento(),
                contenido.getDuracionMinutos(),
                contenido.getSinopsis(),
                contenido.getClasificacionEdad(),
                contenido.getFechaAgregado(),
                "S".equalsIgnoreCase(contenido.getOriginalQuindioflix()),
                contenido.getEmpleadoResponsable().getId(),
                contenido.getPopularidad(),
                contenido.getGeneros().stream()
                        .map(GeneroEntity::getNombre)
                        .sorted()
                        .toList()
        );
    }

    public static ContenidoRelacionadoResponse contenidoRelacionado(ContenidoRelacionadoEntity relacion) {
        return new ContenidoRelacionadoResponse(
                relacion.getId(),
                relacion.getOrigen().getId(),
                relacion.getDestino().getId(),
                relacion.getDestino().getTitulo(),
                relacion.getTipoRelacion(),
                relacion.getDescripcion()
        );
    }

    public static ReproduccionResponse reproduccion(ReproduccionEntity reproduccion) {
        return new ReproduccionResponse(
                reproduccion.getId(),
                reproduccion.getPerfil().getId(),
                reproduccion.getContenido().getId(),
                reproduccion.getEpisodio() == null ? null : reproduccion.getEpisodio().getId(),
                reproduccion.getFechaHoraInicio(),
                reproduccion.getFechaHoraFin(),
                reproduccion.getDispositivo(),
                reproduccion.getPorcentajeAvance()
        );
    }

    public static CalificacionResponse calificacion(CalificacionEntity calificacion) {
        return new CalificacionResponse(
                calificacion.getId(),
                calificacion.getPerfil().getId(),
                calificacion.getContenido().getId(),
                calificacion.getEstrellas(),
                calificacion.getResena(),
                calificacion.getFechaCalificacion()
        );
    }

    public static PagoResponse pago(PagoEntity pago) {
        return new PagoResponse(
                pago.getId(),
                pago.getUsuario().getId(),
                pago.getFechaPago(),
                pago.getMonto(),
                pago.getMetodoPago(),
                pago.getEstadoPago(),
                pago.getReferencia(),
                pago.getDescuentoAplicado()
        );
    }

    public static ReporteContenidoResponse reporte(ReporteContenidoEntity reporte) {
        return new ReporteContenidoResponse(
                reporte.getId(),
                reporte.getUsuarioReporta().getId(),
                reporte.getContenido().getId(),
                reporte.getDescripcion(),
                reporte.getFechaReporte(),
                reporte.getEstado(),
                reporte.getModerador() == null ? null : reporte.getModerador().getId(),
                reporte.getFechaResolucion(),
                reporte.getComentarioResolucion()
        );
    }

    public static EmpleadoResponse empleado(EmpleadoEntity empleado) {
        return new EmpleadoResponse(
                empleado.getId(),
                empleado.getNombre(),
                empleado.getEmail(),
                empleado.getCargo(),
                empleado.getDepartamento() == null ? null : empleado.getDepartamento().getId(),
                empleado.getDepartamento() == null ? null : empleado.getDepartamento().getNombre()
        );
    }
}

