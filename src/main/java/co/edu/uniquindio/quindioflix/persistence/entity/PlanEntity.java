package co.edu.uniquindio.quindioflix.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "PLANES")
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_PLAN")
    private Long id;

    @Column(name = "NOMBRE",nullable = false,unique = true,length = 30)
    private String nombre;

    @Column(name = "PANTALLAS_SIMULTANEAS",nullable = false)
    private Integer pantallasSimultaneas;

    @Column(name = "CALIDAD",nullable = false,length = 10)
    private String calidad;

    @Column(name = "PRECIO_MENSUAL",nullable = false,precision = 10,scale = 2)
    private BigDecimal precioMensual;

    @Column(name = "MAX_PERFILES",nullable = false)
    private Integer maxPerfiles;

    @Column(name = "ACTIVO",nullable = false,length = 1)
    private String activo;

    public boolean activo() {
        return "S".equalsIgnoreCase(activo);

    }

}
