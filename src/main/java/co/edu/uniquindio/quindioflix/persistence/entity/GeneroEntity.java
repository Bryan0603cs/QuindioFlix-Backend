package co.edu.uniquindio.quindioflix.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "GENEROS")
public class GeneroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_GENERO")
    private Long id;
    @Column(name = "NOMBRE",nullable = false,unique = true,length = 60)
    private String nombre;

}
