package dts.com.vn.controller;

import dts.com.vn.entities.CcspInfo;
import dts.com.vn.entities.LogAction;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.CcspInfoRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.service.CcspInfoService;
import dts.com.vn.service.LogActionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/ccsp-info")
public class CcspInfoController {
    private final CcspInfoService ccspInfoService;
    private final TokenProvider tokenProvider;
    private final LogActionService logActionService;

    public CcspInfoController(CcspInfoService ccspInfoService, TokenProvider tokenProvider, LogActionService logActionService) {
        this.ccspInfoService = ccspInfoService;
        this.tokenProvider = tokenProvider;
        this.logActionService = logActionService;
    }

    private static final Logger logger = LoggerFactory.getLogger(CcspInfoController.class);

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveCcspInfo(@RequestBody CcspInfoRequest ccspInfoRequest) {
        ApiResponse response = null;
        try {
            // Tạo Log Action
            LogAction logAction = new LogAction();
            logAction.setTableAction("ccsp_info");
            logAction.setAccount(tokenProvider.account);
            if (ccspInfoRequest.getCcspInfoId() == null) {
                logAction.setAction("CREATE");
                logAction.setOldValue(null);
            } else {
                logAction.setAction("UPDATE");
                logAction.setOldValue(ccspInfoRequest.toString());
            }
            // Save CCSP Info
            CcspInfo ccspInfo = ccspInfoService.saveCcspInfo(ccspInfoRequest);

            logAction.setNewValue(ccspInfo.toString());
            logAction.setTimeAction(new Date());
            logAction.setIdAction(ccspInfo.getCcspInfoId());
            logActionService.add(logAction);
            //
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), ccspInfo);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.SAVE_CCSP_INFO_FAIL);
            logger.error("SAVE_CCSP_INFO_FAIL", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
