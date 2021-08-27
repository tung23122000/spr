package dts.com.vn.controller;

import dts.com.vn.entities.LogAction;
import dts.com.vn.entities.MinusMoney;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.request.MinusMoneyRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.service.LogActionService;
import dts.com.vn.service.MinusMoneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/minus-money")
public class MinusMoneyController {
    private final MinusMoneyService minusMoneyService;
    private final TokenProvider tokenProvider;
    private final LogActionService logActionService;

    private static final Logger logger = LoggerFactory.getLogger(MinusMoneyController.class);

    public MinusMoneyController(MinusMoneyService minusMoneyService, TokenProvider tokenProvider, LogActionService logActionService) {
        this.tokenProvider = tokenProvider;
        this.minusMoneyService = minusMoneyService;
        this.logActionService = logActionService;
    }

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveMinusMoneyLadder(@RequestBody MinusMoneyRequest minusMoneyRequest) {
        ApiResponse response = null;
        try {
            LogAction logAction = new LogAction();
            logAction.setTableAction("minus_money_ladder");
            logAction.setAccount(tokenProvider.account);
            if (minusMoneyRequest.getMinusMoneyLadderId() == null){
                logAction.setAction("CREATE");
                logAction.setOldValue(null);
            }else{
                logAction.setAction("UPDATE");
                logAction.setOldValue(minusMoneyRequest.toString());
            }
            MinusMoney item = minusMoneyService.save(minusMoneyRequest);
            logAction.setIdAction(item.getMinusMoneyLadderId());
            logAction.setNewValue(item.toString());
            logAction.setTimeAction(new Date());
            logActionService.add(logAction);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), item);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.ADD_MINUS_MONEY_FAILED);
            logger.error("UPDATE_PREFIX_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAll(@RequestParam(name = "packageId") Long packageId, @RequestParam(name = "programId") Long programId){
        ApiResponse response = null;
        try {
            List<MinusMoney> list = minusMoneyService.getAll(packageId, programId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.GET_ALL_MINUS_MONEY_FAILED);
            logger.error("GET_ALL_MINUS_MONEY_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @Transactional
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestBody MinusMoney minusMoney){
        ApiResponse response = null;
        try {
            minusMoneyService.delete(minusMoney.getMinusMoneyLadderId());
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
            LogAction logAction = new LogAction();
            logAction.setTableAction("minus_money_ladder");
            logAction.setAccount(tokenProvider.account);
            logAction.setAction("DELETE");
            logAction.setOldValue(minusMoney.toString());
            logAction.setNewValue(null);
            logAction.setTimeAction(new Date());
            logAction.setIdAction(minusMoney.getMinusMoneyLadderId());
            logActionService.add(logAction);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.DELETE_MINUS_MONEY_FAILED);
            logger.error("DELETE_MINUS_MONEY_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }


}
