package dts.com.vn.controller;

import dts.com.vn.entities.PCRFGroup;
import dts.com.vn.entities.PrefixInfo;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.PCRFGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pcrf-group")
public class PCRFGroupController {
    private static final Logger logger = LoggerFactory.getLogger(PCRFGroupController.class);

    private final PCRFGroupService pcrfGroupService;

    public PCRFGroupController(PCRFGroupService pcrfGroupService) {
        this.pcrfGroupService = pcrfGroupService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAll() {
        ApiResponse response = null;
        try {
            List<PCRFGroup> list = pcrfGroupService.getAll();
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.GET_ALL_PCRF_GROUP_FAILED);
            logger.error("GET_ALL_PCRF_GROUP_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/addPCRFGroup")
    public ResponseEntity<ApiResponse> addPCRFGroup(@RequestBody PCRFGroup pcrfGroup) {
        ApiResponse response = null;
        try {
            PCRFGroup pcrf = pcrfGroupService.addPCRFGroup(pcrfGroup);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), pcrf);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.ADD_PCRF_GROUP_FAILED);
            logger.error("ADD_PCRF_GROUP_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
