package co.edu.uniquindio.quindioflix.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "TEMPORADAS", uniqueConstraints = @UniqueConstraint(name = "UK_TEMPORADA_CONTENIDO_NUM", columnNames = {
    "ID_CONTENIDO","NUMERO_TEMPORADA"
}
))
public class TemporadaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_TEMPORADA")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTENIDO",nullable = false)
    private ContenidoEntity contenido;
    @Column(name = "NUMERO_TEMPORADA",nullable = false)
    private Integer numeroTemporada;
    @Column(name = "TITULO",nullable = false,length = 160)
    private String titulo;
    @Column(name = "FECHA_LANZAMIENTO")
    private LocalDate fechaLanzamiento;

}
