package dts.com.vn.request;

import lombok.Data;

import javax.persistence.Column;
import java.time.Instant;

@Data
public class IsdnListRequest {

    private Long isdnListId;

    private String name;

    private String createDate;

    private String cvCodeList;

    private String isDisplay;

    private String endDate;

    private String listType;
}
