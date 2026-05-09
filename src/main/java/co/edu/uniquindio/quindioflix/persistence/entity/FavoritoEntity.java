package co.edu.uniquindio.quindioflix.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "FAVORITOS", uniqueConstraints = @UniqueConstraint(name = "UK_FAVORITO_PERFIL_CONT", columnNames = {
    "ID_PERFIL","ID_CONTENIDO"
}
))
public class FavoritoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_FAVORITO")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PERFIL",nullable = false)
    private PerfilEntity perfil;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTENIDO",nullable = false)
    private ContenidoEntity contenido;
    @Column(name = "FECHA_AGREGADO",nullable = false)
    private LocalDateTime fechaAgregado;

}
