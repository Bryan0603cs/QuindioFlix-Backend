package co.edu.uniquindio.quindioflix.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "CAMBIOS_PLAN")
public class CambioPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_CAMBIO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO",nullable = false)
    private UsuarioEntity usuario;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PLAN_ANTERIOR",nullable = false)
    private PlanEntity planAnterior;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PLAN_NUEVO",nullable = false)
    private PlanEntity planNuevo;
    @Column(name = "FECHA_CAMBIO",nullable = false)
    private LocalDateTime fechaCambio;
    @Column(name = "MOTIVO",length = 200)
    private String motivo;

}
