package dts.com.vn.controller;

import dts.com.vn.entities.IsdnList;
import dts.com.vn.entities.LogAction;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.IsdnListRequest;
import dts.com.vn.request.LogActionRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.IsdnListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/isdn-list")
public class IsdnListController {
    private static final Logger logger = LoggerFactory.getLogger(IsdnListController.class);

    private final IsdnListService isdnListService;

    public IsdnListController(IsdnListService isdnListService) {
        this.isdnListService = isdnListService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse> findAll(Pageable pageable) {
        ApiResponse response;
        try {
            Page<IsdnList> page = isdnListService.findAll(pageable);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), page);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.FIND_ISDN_LIST_FAILED);
            logger.error("FIND_ISDN_LIST_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(@RequestBody IsdnListRequest request) {
        ApiResponse response;
        try {
            IsdnList isdnList = isdnListService.saveIsdnList(request);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), isdnList);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.SAVE_ISDN_LIST_FAILED);
            logger.error("SAVE_ISDN_LIST_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
