package dts.com.vn.request;

import lombok.Data;

@Data
public class GetAllRenewRequest {
    private Long extRetryNum;
    private Long timeResetExtRetryNum;
}
