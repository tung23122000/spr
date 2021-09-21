package dts.com.vn.entities;

import dts.com.vn.request.MapCommandAliasRequest;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "map_command_alias", schema = "public")
public class MapCommandAlias implements Cloneable {
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

    @Column(name = "sms_mo")
    private String smsMo;

    @Column(name = "soap_request")
    private String soapRequest;

    public MapCommandAlias() {
    }

    public MapCommandAlias(MapCommandAliasRequest request, ServiceProgram serviceProgram) {
        this.serviceProgram = serviceProgram;
        this.servicePackage = serviceProgram.getServicePackage();
        this.smsMo = request.getSmsMo();
        this.soapRequest = request.getSoapRequest();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
