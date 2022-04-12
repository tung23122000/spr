package dts.com.vn.response;

import lombok.Data;

import java.util.List;

@Data
public class ReportDailyResponse {

    private Integer totalPage;

    private List<DailyReportResponse> listReport;

}
