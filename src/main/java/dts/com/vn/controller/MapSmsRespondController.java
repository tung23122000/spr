package dts.com.vn.controller;

import dts.com.vn.entities.LogAction;
import dts.com.vn.entities.MapSmsRespond;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.LogActionRequest;
import dts.com.vn.request.MapSmsRespondRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.MapSmsRespondService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map-sms-respond")
public class MapSmsRespondController {
    private final MapSmsRespondService mapSmsRespondService;

    public MapSmsRespondController(MapSmsRespondService mapSmsRespondService) {
        this.mapSmsRespondService = mapSmsRespondService;
    }

    private static final Logger logger = LoggerFactory.getLogger(MapSmsRespondController.class);

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(@RequestBody MapSmsRespondRequest mapSmsRespondRequest) {
        ApiResponse response;
        try {
            MapSmsRespond mapSmsRespond = mapSmsRespondService.save(mapSmsRespondRequest);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), mapSmsRespond);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.SAVE_SMS_RESPOND_FAIL);
            logger.error("SAVE_SMS_RESPOND_FAIL", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse> findAll(@RequestParam Long programId) {
        ApiResponse response;
        try {
            List<MapSmsRespond> mapSmsRespondList = mapSmsRespondService.findAll(programId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), mapSmsRespondList);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.FIND_ALL_SMS_RESPOND_FAIL);
            logger.error("FIND_ALL_SMS_RESPOND_FAIL", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestParam Long mapSmsRespondId) {
        ApiResponse response;
        try {
            mapSmsRespondService.delete(mapSmsRespondId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.DELETE_SMS_RESPOND_FAIL);
            logger.error("DELETE_SMS_RESPOND_FAIL", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
