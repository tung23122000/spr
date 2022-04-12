package dts.com.vn.controller;

import com.google.gson.Gson;
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


    @GetMapping("/getPhoneNumber")
    @ResponseBody
    public ResponseEntity<?> totalPhoneNumer() {
        ApiResponse response;
        try {
            Long totalPhoneNumber = reportService.findAllPhoneNumberHaveActivePackage();
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), totalPhoneNumber, null, "Lấy tổng số thuê bao đang hoạt động thành công.");
            LogUtil.writeLog(logger, LogConstants.RESPONSE, response);
        } catch (Exception e) {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), ErrorCode.REPORT_TOTAL_PHONE_NUMBER_ERROR.getMessage());
            LogUtil.writeLog(logger, LogConstants.ERROR, response);
        }
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/dailyReport")
    @ResponseBody
    public ResponseEntity<ApiResponse> dailyReport1(@RequestParam String date, Integer page) {
        logger.info("=========> " + "groupPackageCode: " + date);
        try {
            ApiResponse response = reportService.dailyReport(date, page);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(e, ErrorCode.DATA_FAILED);
            logger.error("==========> " + LogConstants.LOG_RESPONSE_FAIL + new Gson().toJson(response));
            return ResponseEntity.badRequest().body(response);
        }
    }


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
