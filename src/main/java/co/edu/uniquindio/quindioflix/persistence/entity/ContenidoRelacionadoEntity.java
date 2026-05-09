package co.edu.uniquindio.quindioflix.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "CONTENIDO_RELACIONADO")
public class ContenidoRelacionadoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_RELACION")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTENIDO_ORIGEN",nullable = false)
    private ContenidoEntity origen;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTENIDO_DESTINO",nullable = false)
    private ContenidoEntity destino;
    @Column(name = "TIPO_RELACION",nullable = false,length = 60)
    private String tipoRelacion;
    @Column(name = "DESCRIPCION",length = 500)
    private String descripcion;

}
