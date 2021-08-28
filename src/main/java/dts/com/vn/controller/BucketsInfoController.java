package dts.com.vn.controller;

import dts.com.vn.entities.BucketsInfo;
import dts.com.vn.entities.LogAction;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.AddBucketsInfoRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.BucketsInfoResponse;
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.service.BucketsInfoService;
import dts.com.vn.service.LogActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.function.Function;

@RestController
@RequestMapping("/api/buckets-info")
public class BucketsInfoController {

    @Autowired
    private BucketsInfoService bucketsInfoService;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private LogActionService logActionService;

    private static final Logger logger = LoggerFactory.getLogger(BucketsInfoController.class);

    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse> findAll(@RequestParam(name = "search", required = false) String search, Pageable pageable) {
        ApiResponse response = null;
        try {
            Page<BucketsInfo> page = bucketsInfoService.findAll(search, pageable);
            Page<BucketsInfoResponse> pageResponse =
                    page.map(new Function<BucketsInfo, BucketsInfoResponse>() {
                        @Override
                        public BucketsInfoResponse apply(BucketsInfo service) {
                            return new BucketsInfoResponse(service);
                        }
                    });
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), pageResponse);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ApiResponse(e, ErrorCode.DATA_FAILED);
            logger.error("DATA_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@RequestBody AddBucketsInfoRequest request) {
        ApiResponse response;
        try {
            BucketsInfo entity = bucketsInfoService.add(request);
            //			Tạo Log Action
            LogAction logAction = new LogAction();
            logAction.setTableAction("buckets_info");
            logAction.setAccount(tokenProvider.account);
            logAction.setAction("CREATE");
            logAction.setOldValue(null);
            logAction.setIdAction(entity.getBucketsId());
            logAction.setNewValue(entity.toString());
            logAction.setTimeAction(new Date());
            logActionService.add(logAction);
            //
            BucketsInfoResponse responseEntity = new BucketsInfoResponse(entity);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.ADD_BUCKETS_INFO_FAILED);
            logger.error("ADD_BUCKETS_INFO_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @Transactional
    @PutMapping("/update")
    public ResponseEntity<ApiResponse> update(@RequestBody AddBucketsInfoRequest request) {
        ApiResponse response;
        try {
            // Tạo Log Action
            LogAction logAction = new LogAction();
            logAction.setTableAction("buckets_info");
            logAction.setAccount(tokenProvider.account);
            logAction.setAction("UPDATE");
            logAction.setOldValue(bucketsInfoService.findById(request.getBucketsId()).toString());
            // UPDATE
            BucketsInfo entity = bucketsInfoService.update(request);
            // Sau khi update set New Value
            logAction.setIdAction(entity.getBucketsId());
            logAction.setNewValue(entity.toString());
            logAction.setTimeAction(new Date());
            logActionService.add(logAction);
            BucketsInfoResponse responseEntity = new BucketsInfoResponse(entity);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
            logger.error("UPDATE_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable(name = "id", required = true) Long id) {
        ApiResponse response;
        try {
            BucketsInfo entity = bucketsInfoService.findById(id);
            BucketsInfoResponse responseEntity = new BucketsInfoResponse(entity);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.UPDATE_BUCKETS_INFO_FAILED);
        }
        return ResponseEntity.ok().body(response);
    }

    @Transactional
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestBody AddBucketsInfoRequest addBucketsInfoRequest){
        ApiResponse response = null;
        try {
            BucketsInfo bucketsInfo = bucketsInfoService.findById(addBucketsInfoRequest.getBucketsId());
            // Tạo Log Action
            LogAction logAction = new LogAction();
            logAction.setTableAction("buckets_info");
            logAction.setAccount(tokenProvider.account);
            logAction.setAction("DELETE");
            logAction.setOldValue(bucketsInfo.toString());
            logAction.setNewValue(null);
            logAction.setTimeAction(new Date());
            logAction.setIdAction(bucketsInfo.getBucketsId());
            logActionService.add(logAction);
            //
            bucketsInfoService.delete(bucketsInfo.getBucketsId());
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.DELETE_BUCKETS_INFO_FAILED);
            logger.error("DELETE_BUCKETS_INFO_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
