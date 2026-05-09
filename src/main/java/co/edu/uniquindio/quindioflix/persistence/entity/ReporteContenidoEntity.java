package co.edu.uniquindio.quindioflix.persistence.entity;

import co.edu.uniquindio.quindioflix.business.model.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "REPORTES_CONTENIDO")
public class ReporteContenidoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_REPORTE")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO_REPORTA",nullable = false)
    private UsuarioEntity usuarioReporta;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTENIDO",nullable = false)
    private ContenidoEntity contenido;
    @Column(name = "DESCRIPCION",nullable = false,length = 1000)
    private String descripcion;
    @Column(name = "FECHA_REPORTE",nullable = false)
    private LocalDateTime fechaReporte;
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO",nullable = false,length = 20)
    private EstadoReporte estado;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MODERADOR")
    private UsuarioEntity moderador;
    @Column(name = "FECHA_RESOLUCION")
    private LocalDateTime fechaResolucion;
    @Column(name = "COMENTARIO_RESOLUCION",length = 1000)
    private String comentarioResolucion;

}
