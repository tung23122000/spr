package dts.com.vn.entities;

import dts.com.vn.response.DailyReportResponse;
import lombok.Data;

import java.util.List;

@Data
public class ReportsInsert {

    private Long totalIsdn;

    private List<DailyReportResponse> dailyReport;

}
