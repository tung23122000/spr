package dts.com.vn.controller;

import dts.com.vn.entities.Account;
import dts.com.vn.entities.PrefixInfo;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.PrefixInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prefix")
public class PrefixInfoController {
    private final PrefixInfoService prefixInfoService;

    private static final Logger logger = LoggerFactory.getLogger(PrefixInfoController.class);

    public PrefixInfoController(PrefixInfoService prefixInfoService) {
        this.prefixInfoService = prefixInfoService;
    }

    @GetMapping("/getAllPrefix")
    public ResponseEntity<ApiResponse> getAllPrefix() {
        ApiResponse response = null;
        try {
            List<PrefixInfo> list = prefixInfoService.getAllPrefix();
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.GET_ALL_PREFIX_FAILED);
            logger.error("GET_ALL_PREFIX_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/updatePrefix")
    public ResponseEntity<ApiResponse> updatePrefix(@RequestBody PrefixInfo prefixInfo) {
        ApiResponse response = null;
        try {
            PrefixInfo item = prefixInfoService.updatePrefix(prefixInfo);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), item);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.UPDATE_PREFIX_FAILED);
            logger.error("UPDATE_PREFIX_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
