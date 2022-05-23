package dts.com.vn.response;

import lombok.Data;

@Data
public class InfoIsdnListResponse {

    private Long id;

    private String nameIsdnList;

    private String nameTargetFolder;

    private String nameTargetSystem;

    private String user;

    private String password;

    private Long isdnListId;

}
