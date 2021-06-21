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
    @Column(name = "cmd_alias_id")
    private Long cmdAliasId;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private ServiceProgram serviceProgram;

    @Column(name = "cmd_trans_code")
    private String cmdTransCode;

    @Column(name = "cmd_alias_name")
    private String cmdAliasName;

    @Column(name = "cmd_status")
    private String cmdStatus;

    public MapCommandAlias() {
    }

    public MapCommandAlias(MapCommandAliasRequest request, ServiceProgram serviceProgram) {
        this.serviceProgram = serviceProgram;
        this.servicePackage = serviceProgram.getServicePackage();
        this.cmdTransCode = request.getCmdTransCode();
        this.cmdAliasName = request.getCmdAliasName();
        if (request.getCmdStatus()){
            this.cmdStatus = "1";
        }else{
            this.cmdStatus = "0";
        }
    }
}