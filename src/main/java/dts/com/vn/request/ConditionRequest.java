package dts.com.vn.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class ConditionRequest {
    private Long conditionId;

    private String conditionName;

    private Boolean isConfirm;

    private String messageMt;

    private Boolean isSoapConfirm;
}
