package dts.com.vn.request;

import lombok.Data;

@Data
public class MapSmsRespondRequest {
    private Long mapSmsRespondId;

    private String smsRespond;

    private String mapSmsRespond;

    private String description;

    private String shortcode;

    private Long programId;

    private String sourceVas;
}
