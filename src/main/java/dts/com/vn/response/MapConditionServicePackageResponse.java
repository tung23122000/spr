package dts.com.vn.response;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class MapConditionServicePackageResponse {
    private Long id;

    private Long packageId;

    private Long programId;

    private Long conditionId;

    private Boolean isConfirm;

    private String messageMt;

    private String conditionName;
}
