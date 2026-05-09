package co.edu.uniquindio.quindioflix.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "DEPARTAMENTOS")
public class DepartamentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_DEPARTAMENTO")
    private Long id;
    @Column(name = "NOMBRE",nullable = false,unique = true,length = 80)
    private String nombre;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_JEFE")
    private EmpleadoEntity jefe;

}
