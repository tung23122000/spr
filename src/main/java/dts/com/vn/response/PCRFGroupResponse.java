package dts.com.vn.response;

import dts.com.vn.entities.PCRFGroup;
import lombok.Data;

@Data
public class PCRFGroupResponse {
    private Long pcrfGroupId;

    private String pcrfGroupName;

    public PCRFGroupResponse(PCRFGroup pcrfGroup) {
        this.pcrfGroupId = pcrfGroup.getPcrfGroupId();
        this.pcrfGroupName = pcrfGroup.getPcrfGroupName();
    }
}
