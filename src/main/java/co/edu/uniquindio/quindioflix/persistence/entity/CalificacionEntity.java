package co.edu.uniquindio.quindioflix.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "CALIFICACIONES", uniqueConstraints = @UniqueConstraint(name = "UK_CALIFICACION_PERFIL_CONT", columnNames = {
    "ID_PERFIL","ID_CONTENIDO"
}
))
public class CalificacionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_CALIFICACION")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERFIL",nullable = false)
    private PerfilEntity perfil;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTENIDO",nullable = false)
    private ContenidoEntity contenido;
    @Column(name = "ESTRELLAS",nullable = false)
    private Integer estrellas;
    @Column(name = "RESENA",length = 1000)
    private String resena;
    @Column(name = "FECHA_CALIFICACION",nullable = false)
    private LocalDateTime fechaCalificacion;

}
