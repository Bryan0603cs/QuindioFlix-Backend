package co.edu.uniquindio.quindioflix.persistence.entity;

import co.edu.uniquindio.quindioflix.business.model.*;
import jakarta.persistence.*;
import lombok.*;
import java.time.*;
import java.math.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "PAGOS")
public class PagoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_PAGO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO",nullable = false)
    private UsuarioEntity usuario;
    @Column(name = "FECHA_PAGO",nullable = false)
    private LocalDateTime fechaPago;
    @Column(name = "MONTO",nullable = false,precision = 10,scale = 2)
    private BigDecimal monto;
    @Enumerated(EnumType.STRING)
    @Column(name = "METODO_PAGO",nullable = false,length = 30)
    private MetodoPago metodoPago;
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_PAGO",nullable = false,length = 20)
    private EstadoPago estadoPago;
    @Column(name = "REFERENCIA",length = 120)
    private String referencia;
    @Column(name = "DESCUENTO_APLICADO",nullable = false,precision = 10,scale = 2)
    private BigDecimal descuentoAplicado;

}
