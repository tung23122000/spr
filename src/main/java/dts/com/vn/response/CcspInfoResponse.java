package dts.com.vn.response;

import dts.com.vn.entities.CcspInfo;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

import java.util.Objects;

@Data
public class CcspInfoResponse {

    private Long ccspInfoId;

    private Long servicePackageId;

    private Long serviceProgramId;

    private String foName;

    private String ccspValue;

    private String description;

    private String startDate;

    private String endDate;

    public CcspInfoResponse(CcspInfo ccspInfo) {
        this.ccspInfoId = ccspInfo.getCcspInfoId();
        this.servicePackageId = ccspInfo.getServicePackage().getPackageId();
        this.serviceProgramId = ccspInfo.getServiceProgram().getProgramId();
        this.foName = ccspInfo.getFoName();
        this.ccspValue = ccspInfo.getCcspValue();
        this.description = ccspInfo.getDescription();
        this.startDate = Objects.nonNull(ccspInfo.getStartDate())
                ? DateTimeUtil.formatInstant(ccspInfo.getStartDate(), "dd/MM/yyyy HH:mm:ss")
                : "";
        this.endDate = Objects.nonNull(ccspInfo.getEndDate())
                ? DateTimeUtil.formatInstant(ccspInfo.getEndDate(), "dd/MM/yyyy HH:mm:ss")
                : "";
    }
}
