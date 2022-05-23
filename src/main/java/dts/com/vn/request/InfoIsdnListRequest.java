package dts.com.vn.request;

import lombok.Data;

@Data
public class InfoIsdnListRequest {

    private String nameTargetFolder;

    private String nameTargetSystem;

    private String user;

    private String password;

    private Long isdnListId;

}
