package dts.com.vn.request;

import lombok.Data;

@Data
public class MapConditionProgramRequest {
    private Long id;

    private Long programId;

    private  Long conditionId;

    private String conditionValue;

}
