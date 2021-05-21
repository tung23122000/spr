package dts.com.vn.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MapConditionServicePackageRequest {
    private List<ConditionRequest> listCondition;

    private Long packageId;

    private Long programId;

}
