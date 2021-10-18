package dts.com.vn.entities;

import dts.com.vn.request.CcspInfoRequest;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "ccsp_info", schema = "public")
public class CcspInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ccsp_info_id")
    private Long ccspInfoId;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private ServiceProgram serviceProgram;

    @Column(name = "fo_name")
    private String foName;

    @Column(name = "ccsp_value")
    private String ccspValue;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    public CcspInfo() {

    }

    public CcspInfo(CcspInfoRequest request, ServicePackage servicePackage, ServiceProgram serviceProgram) {
        this.ccspInfoId = request.getCcspInfoId();
        this.servicePackage = servicePackage;
        this.serviceProgram = serviceProgram;
        this.foName = request.getFoName();
        this.ccspValue = request.getCcspValue();
        this.description = request.getDescription();
        this.startDate = DateTimeUtil.convertStringToInstant(request.getStartDate(), "dd/MM/yyyy HH:mm:ss");
        this.endDate = DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss");
    }
}
