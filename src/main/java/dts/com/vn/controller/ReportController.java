package dts.com.vn.controller;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.enumeration.LogConstants;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ReportService;
import dts.com.vn.util.LogUtil;
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
	public ResponseEntity<ApiResponse> dailyReport(@RequestParam Long serviceTypeId, @RequestParam String date) {
		logger.info("=========> " + "groupPackageCode: " + serviceTypeId);
		ApiResponse response;
		try {
			response = reportService.dailyReport(serviceTypeId, date);
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/getPhoneNumber")
	@ResponseBody
	public ResponseEntity<?> totalPhoneNumer() {
		ApiResponse response;
		try {
			Long totalPhoneNumber = reportService.findPhoneNumber();
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), totalPhoneNumber, null, "Lấy tổng số thuê bao đang hoạt động thành công.");
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.REPORT_TOTAL_PHONE_NUMBER_ERROR);
			LogUtil.writeLog(logger, LogConstants.ERROR, response);
		}
		return ResponseEntity.ok().body(response);
	}

	/**
	 * Description - Controller lấy thông tin package
	 *
	 * @author - binhDT
	 * @created - 17/01/2022
	 */
//	@GetMapping("/dailyReport/find-package")
//	@ResponseBody
//	public ResponseEntity<ApiResponse> findByPackageAndTransaction(@RequestParam Long serviceTypeId, @RequestParam String packageCode, @RequestParam String transaction, @RequestParam String date) {
//		logger.info("=========> " + "groupPackageCode: " + serviceTypeId);
//		ApiResponse response;
//		try {
//			response = reportService.findByPackageAndTransaction(serviceTypeId, packageCode, transaction, date);
//		} catch (RestApiException ex) {
//			ex.printStackTrace();
//			response = new ApiResponse(ex);
//		}
//		return ResponseEntity.ok().body(response);
//	}

	@GetMapping("/daily-top-10-isdn")
	@ResponseBody
	public ResponseEntity<ApiResponse> dailyTop10IsdnReport(@RequestParam String date) {
		logger.info("=========> " + "logger");
		ApiResponse response;
		try {
			response = reportService.dailyTop10IsdnReport(date);
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex);
		}
		return ResponseEntity.ok().body(response);
	}
}
