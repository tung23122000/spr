package dts.com.vn.controller;

import dts.com.vn.entities.Register;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.RegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);

    @GetMapping("/reset-ext-retry-num")
    public ResponseEntity<ApiResponse> resetExtRetryNum(@RequestParam(value="extRetryNum") Long extRetryNum, @RequestParam(value="timeResetExtRetryNum") Integer timeResetExtRetryNum) {
        ApiResponse response;
        try {
            registerService.resetExtRetryNum(extRetryNum, timeResetExtRetryNum);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.RESET_EXT_RETRY_NUM_FAILED);
            logger.error("RESET_EXT_RETRY_NUM_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

}
