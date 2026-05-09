package co.edu.uniquindio.quindioflix.persistence.entity;

import co.edu.uniquindio.quindioflix.business.model.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.util.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "CONTENIDO")
public class ContenidoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_CONTENIDO")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CATEGORIA",nullable = false)
    private CategoriaEntity categoria;

    @Column(name = "TITULO",nullable = false,length = 200)
    private String titulo;
    @Column(name = "ANIO_LANZAMIENTO",nullable = false)
    private Integer anioLanzamiento;

    @Column(name = "DURACION_MINUTOS",nullable = false)
    private Integer duracionMinutos;
    @Column(name = "SINOPSIS",length = 2000)
    private String sinopsis;

    @Enumerated(EnumType.STRING)
    @Column(name = "CLASIFICACION_EDAD",nullable = false,length = 20)
    private ClasificacionEdad clasificacionEdad;

    @Column(name = "FECHA_AGREGADO",nullable = false)
    private LocalDate fechaAgregado;
    @Column(name = "ORIGINAL_QUINDIOFLIX",nullable = false,length = 1)
    private String originalQuindioflix;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_EMPLEADO_RESPONSABLE",nullable = false)
    private EmpleadoEntity empleadoResponsable;

    @Column(name = "POPULARIDAD",nullable = false)
    private Integer popularidad;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "CONTENIDO_GENERO",
            joinColumns = @JoinColumn(name = "ID_CONTENIDO"),
            inverseJoinColumns = @JoinColumn(name = "ID_GENERO")
    )
    private Set<GeneroEntity> generos = new HashSet<>();

}
