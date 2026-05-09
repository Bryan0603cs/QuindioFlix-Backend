package co.edu.uniquindio.quindioflix.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "EPISODIOS", uniqueConstraints = @UniqueConstraint(name = "UK_EP_TEMP_NUM", columnNames = {
    "ID_TEMPORADA","NUMERO_EPISODIO"
}
))
public class EpisodioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_EPISODIO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TEMPORADA",nullable = false)
    private TemporadaEntity temporada;
    @Column(name = "NUMERO_EPISODIO",nullable = false)
    private Integer numeroEpisodio;
    @Column(name = "TITULO",nullable = false,length = 200)
    private String titulo;
    @Column(name = "DURACION_MINUTOS",nullable = false)
    private Integer duracionMinutos;
    @Column(name = "SINOPSIS",length = 2000)
    private String sinopsis;

}
