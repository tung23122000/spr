package dts.com.vn.controller;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.controller.LKTCheckConditionController;
import dts.com.vn.request.InfoIsdnListRequest;
import dts.com.vn.request.InfoTargetSystemRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.InfoIsdnListResponse;
import dts.com.vn.response.InfoTargetSystemResponse;
import dts.com.vn.service.InfoTargetSystemService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class InfoTargetSystemController {

    private static final Logger logger = LoggerFactory.getLogger(LKTCheckConditionController.class);

    private final InfoTargetSystemService infoTargetSystemService;

    public InfoTargetSystemController(
            InfoTargetSystemService infoTargetSystemService) {this.infoTargetSystemService = infoTargetSystemService;}

    @ApiOperation(value = "Lấy danh sách info")
    @GetMapping("/find-all-info-target-system")
    @ResponseBody
    public ResponseEntity<ApiResponse> findAllInfo() {
        ApiResponse response;
        try {
            response = infoTargetSystemService.findAllInfo();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ApiOperation(value = "Thêm mới bản ghi vào danh sách info")
    @PostMapping("/create-info-target-system")
    @ResponseBody
    public ResponseEntity<ApiResponse> createInfo(@RequestBody InfoTargetSystemRequest request) {
        ApiResponse response;
        try {
            response = infoTargetSystemService.createInfoTargetSystem(request);
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ApiOperation(value = "Chi tiết một bản ghi trong danh sách info")
    @PostMapping("/get-detail-info-target-system")
    @ResponseBody
    public ResponseEntity<ApiResponse> createInfo(@RequestBody Long id) {
        ApiResponse response;
        try {
            response = infoTargetSystemService.getDetail(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ApiOperation(value = "Cập nhật một bản ghi trong danh sách info")
    @PostMapping("/update-info-target-system")
    @ResponseBody
    public ResponseEntity<ApiResponse> updateInfo(@RequestBody InfoTargetSystemResponse request) {
        ApiResponse response;
        try {
            response = infoTargetSystemService.updateInfo(request);
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ApiOperation(value = "Xoá một bản ghi trong danh sách info")
    @PostMapping("/delete-info-target-system")
    @ResponseBody
    public ResponseEntity<ApiResponse> deleteInfo(@RequestBody Long id) {
        ApiResponse response;
        try {
            response = infoTargetSystemService.deleteInfo(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ApiOperation(value = "Lấy danh sách tên hệ thống đích")
    @GetMapping("/find-all-system-name")
    @ResponseBody
    public ResponseEntity<ApiResponse> getAllSystemsNames() {
        ApiResponse response;
        try {
            response = infoTargetSystemService.getAllSystemsNames();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
