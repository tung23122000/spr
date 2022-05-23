package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "info_target_system", schema = "public")
public class InfoTargetSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name_target_system")
    private String nameTargetSystem;

    @Column(name = "ip_target_system")
    private String ipTargetSystem;

    @Column(name = "port_target_system")
    private Integer portTargetSystem;

}
