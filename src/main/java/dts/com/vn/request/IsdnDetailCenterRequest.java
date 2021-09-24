package dts.com.vn.request;

import lombok.Data;

@Data
public class IsdnDetailCenterRequest {
    private Long isdnDetailCenterId;

    private String centerId;

    private String isdnPrefix;

    private String network;
}
