package dts.com.vn.response.ilink;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ILRequestParam {

    private Long requestId;

    private Integer taskId;

    private Integer paramType;

    private String paramName;

    private String paramValue;

}
