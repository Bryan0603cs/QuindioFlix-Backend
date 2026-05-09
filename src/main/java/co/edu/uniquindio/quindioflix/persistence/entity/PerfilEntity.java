package co.edu.uniquindio.quindioflix.persistence.entity;

import co.edu.uniquindio.quindioflix.business.model.*;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter @NoArgsConstructor
@AllArgsConstructor @Builder
@Entity @Table(name = "PERFILES", uniqueConstraints = @UniqueConstraint(name = "UK_PERFIL_USUARIO_NOMBRE", columnNames = {
    "ID_USUARIO","NOMBRE"
}
))
public class PerfilEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "ID_PERFIL")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO",nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "NOMBRE",nullable = false,length = 60)
    private String nombre;

    @Column(name = "AVATAR",length = 255)
    private String avatar;

    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_PERFIL",nullable = false,length = 20)
    private TipoPerfil tipoPerfil;

    public boolean infantil() {
        return tipoPerfil==TipoPerfil.INFANTIL;

    }

}
