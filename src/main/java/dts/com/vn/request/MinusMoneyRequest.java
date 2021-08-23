package dts.com.vn.request;

import lombok.Data;

@Data
public class MinusMoneyRequest {
    private Long minusMoneyLadderId;

    private Long servicePackage;

    private Long serviceProgram;

    private Long minusAmount;

    private Long expiredDay;
}
