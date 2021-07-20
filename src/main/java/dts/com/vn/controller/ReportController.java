package dts.com.vn.controller;

import dts.com.vn.exception.RestApiException;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ReportController {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

	private final ReportService reportService;

	@Autowired
	public ReportController(ReportService reportService) {
		this.reportService = reportService;
	}

	@GetMapping("/dailyReport")
	@ResponseBody
	public ResponseEntity<ApiResponse> dailyReport(@RequestParam Long serviceTypeId,
	                                               @RequestParam String startDate,
	                                               @RequestParam String endDate) {
		logger.info("=========> " + "groupPackageCode: " + serviceTypeId);
		ApiResponse response;
		try {
			response = reportService.dailyReport(serviceTypeId, startDate, endDate);
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex);
		}
		return ResponseEntity.ok().body(response);
	}

}
