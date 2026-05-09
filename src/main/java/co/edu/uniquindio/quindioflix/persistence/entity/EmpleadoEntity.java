package co.edu.uniquindio.quindioflix.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "EMPLEADOS")
public class EmpleadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_EMPLEADO")
    private Long id;

    @Column(name = "NOMBRE",nullable = false,length = 120)
    private String nombre;
    @Column(name = "EMAIL",nullable = false,unique = true,length = 150)
    private String email;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DEPARTAMENTO",nullable = false)
    private DepartamentoEntity departamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUPERVISOR")
    private EmpleadoEntity supervisor;

    @Column(name = "CARGO",nullable = false,length = 80)
    private String cargo;
    @Column(name = "ACTIVO",nullable = false,length = 1)
    private String activo;

}
