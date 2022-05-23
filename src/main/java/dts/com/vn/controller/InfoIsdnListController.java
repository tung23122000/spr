package dts.com.vn.controller;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.controller.LKTCheckConditionController;
import dts.com.vn.request.InfoIsdnListRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.InfoIsdnListResponse;
import dts.com.vn.service.InfoIsdnListService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class InfoIsdnListController {

    private final InfoIsdnListService infoIsdnListService;
    private static final Logger logger = LoggerFactory.getLogger(LKTCheckConditionController.class);
    public InfoIsdnListController(
            InfoIsdnListService infoIsdnListService) {this.infoIsdnListService = infoIsdnListService;}

    @ApiOperation(value = "Lấy danh sách info")
    @GetMapping("/find-all-info-isdn-list")
    @ResponseBody
    public ResponseEntity<ApiResponse> findAllInfo() {
        ApiResponse response;
        try {
            response = infoIsdnListService.findAllInfo();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ApiOperation(value = "Thêm mới bản ghi vào danh sách info")
    @PostMapping("/create-info-isdn-list")
    @ResponseBody
    public ResponseEntity<ApiResponse> createInfo(@RequestBody InfoIsdnListRequest request) {
        ApiResponse response;
        try {
            response = infoIsdnListService.createInfo(request);
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ApiOperation(value = "Chi tiết một bản ghi trong danh sách info")
    @PostMapping("/get-detail-info-isdn-list")
    @ResponseBody
    public ResponseEntity<ApiResponse> createInfo(@RequestBody Long id) {
        ApiResponse response;
        try {
            response = infoIsdnListService.getDetailInfo(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ApiOperation(value = "Cập nhật một bản ghi trong danh sách info")
    @PostMapping("/update-info-isdn-list")
    @ResponseBody
    public ResponseEntity<ApiResponse> updateInfo(@RequestBody InfoIsdnListResponse request) {
        ApiResponse response;
        try {
            response = infoIsdnListService.updateInfo(request);
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ApiOperation(value = "Xoá một bản ghi trong danh sách info")
    @PostMapping("/delete-info-isdn-list")
    @ResponseBody
    public ResponseEntity<ApiResponse> deleteInfo(@RequestBody Long id) {
        ApiResponse response;
        try {
            response = infoIsdnListService.deleteInfo(id);
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @ApiOperation(value = "Lấy danh sách folder tồn tại trên server")
    @GetMapping("/find-all-isdn-list-id-exist")
    @ResponseBody
    public ResponseEntity<ApiResponse> getListIsdnListId() {
        ApiResponse response;
        try {
            response = infoIsdnListService.getListIsdnListId();
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

}
