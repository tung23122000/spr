package dts.com.vn.response;

import lombok.Data;

@Data
public class Report5Response {

    private Long totalPackageDelete;

    private Long totalPackageActiveOrRenew;

    private Long totalPackageWaitRetry;

    private Long totalPackageActive;

    private Long moneyMinusPerDay;

}
