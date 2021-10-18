package dts.com.vn.request;

import lombok.Data;

@Data
public class CcspInfoRequest {
    private Long ccspInfoId;

    private Long servicePackageId;

    private Long serviceProgramId;

    private String foName;

    private String ccspValue;

    private String description;

    private String startDate;

    private String endDate;
}
