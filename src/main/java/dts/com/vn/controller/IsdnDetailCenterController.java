package dts.com.vn.controller;

import dts.com.vn.entities.IsdnDetailCenter;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.AddServicePackageRequest;
import dts.com.vn.request.IsdnDetailCenterRequest;
import dts.com.vn.request.LogActionRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.IsdnDetailCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/isdn-detail-center")
public class IsdnDetailCenterController {
    private final IsdnDetailCenterService isdnDetailCenterService;

    public IsdnDetailCenterController(IsdnDetailCenterService isdnDetailCenterService) {
        this.isdnDetailCenterService = isdnDetailCenterService;
    }

    private static final Logger logger = LoggerFactory.getLogger(IsdnDetailCenterController.class);

    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse> findAll() {
        ApiResponse response;
        try {
            List<IsdnDetailCenter> page = isdnDetailCenterService.findAll();
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), page);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.FIND_LOG_ACTION_FAILED);
            logger.error("FIND_ISDN_DETAIL_CENTER_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@RequestBody IsdnDetailCenterRequest request) {
        ApiResponse response = null;
        try {
            IsdnDetailCenter isdnDetailCenter = isdnDetailCenterService.add(request);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), isdnDetailCenter);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.ADD_ISDN_DETAIL_CENTER);
            logger.error("ADD_ISDN_DETAIL_CENTER", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> update(@RequestBody IsdnDetailCenterRequest request) {
        ApiResponse response = null;
        try {
            IsdnDetailCenter isdnDetailCenter = isdnDetailCenterService.update(request);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), isdnDetailCenter);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.UPDATE_ISDN_DETAIL_CENTER);
            logger.error("UPDATE_ISDN_DETAIL_CENTER", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestParam Long id) {
        ApiResponse response = null;
        try {
            isdnDetailCenterService.delete(id);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.DELETE_ISDN_DETAIL_CENTER);
            logger.error("DELETE_ISDN_DETAIL_CENTER", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
