package dts.com.vn.request;

import dts.com.vn.entities.JsonIsdn;
import lombok.Data;

import java.util.List;

@Data
public class ServicePackageListRequest {
    private String fileName; // file import isdn
    private List<JsonIsdn> listIsdn; // list isdn
    private Long packageId;
    private Long programId;
    private String staDate;
    private String endDate;


}
