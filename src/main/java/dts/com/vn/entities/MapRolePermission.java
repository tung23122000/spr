package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "map_role_permission", schema = "public")
public class MapRolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @OneToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
