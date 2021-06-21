package dts.com.vn.entities;

import dts.com.vn.request.MapCommandAliasRequest;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "map_command_alias", schema = "public")
public class MapCommandAlias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "map_command_alias_id")
    private Long mapCommandAliasId;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private ServiceProgram serviceProgram;

    @Column(name = "trans_code")
    private String transCode;

    @Column(name = "command_alias")
    private String commandAlias;

    @Column(name = "is_active")
    private Boolean isActive;

    public MapCommandAlias() {
    }

    public MapCommandAlias(MapCommandAliasRequest request, ServiceProgram serviceProgram) {
        this.serviceProgram = serviceProgram;
        this.servicePackage = serviceProgram.getServicePackage();
        this.transCode = request.getTransCode();
        this.commandAlias = request.getCommandAlias();
        this.isActive = request.getIsActive();
    }
}
