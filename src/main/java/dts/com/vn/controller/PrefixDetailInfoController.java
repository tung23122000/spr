package dts.com.vn.controller;

import dts.com.vn.entities.PrefixDetailInfo;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.PrefixDetailInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prefix-detail")
public class PrefixDetailInfoController {

    private static final Logger logger = LoggerFactory.getLogger(PrefixDetailInfoController.class);

    private final PrefixDetailInfoService prefixDetailInfoService;

    public PrefixDetailInfoController(PrefixDetailInfoService prefixDetailInfoService) {
        this.prefixDetailInfoService = prefixDetailInfoService;
    }

    @GetMapping("/getPrefixDetailById/{prefixId}")
    public ResponseEntity<ApiResponse> getPrefixDetailById(@PathVariable("prefixId") Long prefixId){
        ApiResponse response = null;
        try {
            List<PrefixDetailInfo> item = prefixDetailInfoService.getPrefixDetailById(prefixId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), item);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.GET_PREFIX_DETAIL_BY_ID);
            logger.error("GET_PREFIX_DETAIL_BY_ID", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/addPrefixDetail")
    public ResponseEntity<ApiResponse> addPrefixDetail(@RequestBody PrefixDetailInfo prefixDetailInfo){
        ApiResponse response = null;
        try {
            PrefixDetailInfo item = prefixDetailInfoService.addPrefixDetail(prefixDetailInfo);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), item);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.ADD_PREFIX_DETAIL_FAILED);
            logger.error("ADD_PREFIX_DETAIL_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
