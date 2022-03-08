package dts.com.vn.request;

import lombok.Data;

@Data
public class NewListConditionRequest {
    private String conditionName;
    private String ilinkServiceName;
    private Boolean isPackage;
}
