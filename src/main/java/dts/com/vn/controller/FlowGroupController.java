package dts.com.vn.controller;

import dts.com.vn.entities.FlowGroup;
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.ServiceProgramResponse;
import dts.com.vn.service.FlowGroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/flow-group")
public class FlowGroupController {

    private final FlowGroupService flowGroupService;

    public FlowGroupController(FlowGroupService flowGroupService) {
        this.flowGroupService = flowGroupService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse> findAll() {
        ApiResponse response = null;
        try {
            List<FlowGroup> page = flowGroupService.findAll();
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), page);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
        }
        return ResponseEntity.ok().body(response);
    }
}
