package dts.com.vn.ilink.dto;

import lombok.Data;

@Data
public class ConditionResponse {

    private String conditionName;

    private String ilinkServiceName;

    private String order;

    private Boolean isSPR;

}
