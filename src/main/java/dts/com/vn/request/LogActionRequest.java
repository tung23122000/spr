package dts.com.vn.request;

import lombok.Data;

@Data
public class LogActionRequest {

    private String action;

    private String startDate;

    private String endDate;
}
