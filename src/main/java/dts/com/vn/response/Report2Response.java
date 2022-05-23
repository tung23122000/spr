package dts.com.vn.response;

import lombok.Data;

@Data
public class Report2Response {

    private Long totalNeedRetry;

    private Long totalRetried;

    private String ratioRetriedAndRetry;

}
