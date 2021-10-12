package dts.com.vn.request;

import lombok.Data;

import java.util.List;

@Data
public class MapIsdnListRequest {
    private Long programId;

    private Long packageId;

    private List<MapIsdnList> listMapIsdn;
}
