package dts.com.vn.response;

import lombok.Data;

@Data
public class Report4Response {

    private String packageCode;

    private Long totalExtRetryEqualThree;

    private Long totalNotProcessedyet;

}
