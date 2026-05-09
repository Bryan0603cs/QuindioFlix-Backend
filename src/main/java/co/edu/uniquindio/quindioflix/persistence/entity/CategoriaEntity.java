package co.edu.uniquindio.quindioflix.persistence.entity;

import co.edu.uniquindio.quindioflix.business.model.*;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "CATEGORIAS")
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_CATEGORIA")
    private Long id;

    @Column(name = "NOMBRE",nullable = false,unique = true,length = 50)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_CONTENIDO",nullable = false,length = 20)
    private TipoContenido tipoContenido;

}
