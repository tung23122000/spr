package dts.com.vn.response;

import lombok.Data;

@Data
public class RenewFailedResponse {

    private String packageCode;

    private Long totalExtRetryEqualThree;

    private Long totalNotProcessedyet;

}
