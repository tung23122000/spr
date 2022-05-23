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


    @GetMapping("/dailyReport")
    @ResponseBody
    public ResponseEntity<ApiResponse> getReport1(@RequestParam String date) {
        logger.info("=========> " + "groupPackageCode: " + date);
        try {
            ApiResponse response = reportService.getReport1(date);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            ApiResponse response = new ApiResponse(e, ErrorCode.DATA_FAILED);
            logger.error("==========> " + LogConstants.LOG_RESPONSE_FAIL + new Gson().toJson(response));
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/report-retry-renew-package")
    @ResponseBody
    public ResponseEntity<ApiResponse> getReport2(@RequestParam String date) {
        logger.info("=========> " + "logger");
        ApiResponse response;
        try {
            response = reportService.getReport2(date);
        } catch (RestApiException ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex);
        }
        return ResponseEntity.ok().body(response);
    }


    @GetMapping("/daily-top-10-isdn")
    @ResponseBody
    public ResponseEntity<ApiResponse> getReport3(@RequestParam String date) {
        logger.info("=========> " + "logger");
        ApiResponse response;
        try {
            response = reportService.getReport3(date);
        } catch (RestApiException ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/report-renew-failed")
    @ResponseBody
    public ResponseEntity<ApiResponse> reportRenewFaildYesterday(@RequestParam String date) {
        logger.info("=========> " + "logger");
        ApiResponse response;
        try {
            response = reportService.getReport4(date);
        } catch (RestApiException ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/report-data-system")
    @ResponseBody
    public ResponseEntity<ApiResponse> reportRetryDataSystem(@RequestParam String date) {
        logger.info("=========> " + "logger");
        ApiResponse response;
        try {
            response = reportService.getReport5(date);
        } catch (RestApiException ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex);
        }
        return ResponseEntity.ok().body(response);
    }

}
