package co.edu.uniquindio.quindioflix.persistence.entity;

import co.edu.uniquindio.quindioflix.business.model.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "REPRODUCCIONES")
public class ReproduccionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_REPRODUCCION")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERFIL",nullable = false)
    private PerfilEntity perfil;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTENIDO",nullable = false)
    private ContenidoEntity contenido;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EPISODIO")
    private EpisodioEntity episodio;
    @Column(name = "FECHA_HORA_INICIO",nullable = false)
    private LocalDateTime fechaHoraInicio;
    @Column(name = "FECHA_HORA_FIN")
    private LocalDateTime fechaHoraFin;
    @Enumerated(EnumType.STRING)
    @Column(name = "DISPOSITIVO",nullable = false,length = 20)
    private Dispositivo dispositivo;
    @Column(name = "PORCENTAJE_AVANCE",nullable = false)
    private Integer porcentajeAvance;

}
