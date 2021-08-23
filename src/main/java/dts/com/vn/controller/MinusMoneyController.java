package dts.com.vn.controller;

import dts.com.vn.entities.MinusMoney;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.request.MinusMoneyRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.MinusMoneyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/minus-money")
public class MinusMoneyController {
    private final MinusMoneyService minusMoneyService;

    private static final Logger logger = LoggerFactory.getLogger(MinusMoneyController.class);

    public MinusMoneyController(MinusMoneyService minusMoneyService) {
        this.minusMoneyService = minusMoneyService;
    }

//    @GetMapping("/getAllPrefix")
//    public ResponseEntity<ApiResponse> getAllPrefix() {
//        ApiResponse response = null;
//        try {
//            List<PrefixInfo> list = prefixInfoService.getAllPrefix();
//            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            response = new ApiResponse(ex, ErrorCode.GET_ALL_PREFIX_FAILED);
//            logger.error("GET_ALL_PREFIX_FAILED", response);
//        }
//        return ResponseEntity.ok().body(response);
//    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> saveMinusMoneyLadder(@RequestBody MinusMoneyRequest minusMoneyRequest) {
        ApiResponse response = null;
        try {
            MinusMoney item = minusMoneyService.save(minusMoneyRequest);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), item);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.ADD_MINUS_MONEY_FAILED);
            logger.error("UPDATE_PREFIX_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
//    @RequestParam(name = "name") String customName

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse> getAll(@RequestParam(name = "packageId") Long packageId, @RequestParam(name = "programId") Long programId){
        ApiResponse response = null;
        try {
            List<MinusMoney> list = minusMoneyService.getAll(packageId, programId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.ADD_MINUS_MONEY_FAILED);
            logger.error("UPDATE_PREFIX_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }




}
