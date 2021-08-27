package dts.com.vn.controller;

import dts.com.vn.entities.LogAction;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.LogActionRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.ServicePackageResponse;
import dts.com.vn.service.LogActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/log-action")
public class LogActionController {

    private static final Logger logger = LoggerFactory.getLogger(LogActionController.class);

    private final LogActionService logActionService;

    public LogActionController(LogActionService logActionService) {
        this.logActionService = logActionService;
    }

    @PostMapping("/find-all")
    public ResponseEntity<ApiResponse> findAll(@RequestBody LogActionRequest logActionRequest, Pageable pageable) {
        ApiResponse response;
        try {
            Page<LogAction> page = logActionService.findAll(logActionRequest, pageable);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), page);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.FIND_LOG_ACTION_FAILED);
            logger.error("FIND_LOG_ACTION_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
