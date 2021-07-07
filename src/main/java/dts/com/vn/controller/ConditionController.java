package dts.com.vn.controller;

import dts.com.vn.entities.Condition;
import dts.com.vn.entities.MapConditionServicePackage;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.request.ConditionRequest;
import dts.com.vn.request.MapConditionServicePackageRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ConditionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/condition")
public class ConditionController {

    private final ConditionService conditionService;

    private static final Logger logger = LoggerFactory.getLogger(ConditionController.class);

    @Autowired
    public ConditionController(ConditionService conditionService) {
        this.conditionService = conditionService;
    }

    @GetMapping("/find-all")
    public ResponseEntity<?> findAll() {
        ApiResponse response;
        try {
            List<Condition> list = conditionService.findAll();
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.CONDITION_NOT_LOADED);
            logger.error("CONDITION_NOT_LOADED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @PostMapping("/save-condition")
    public ResponseEntity<?> saveCondition(@RequestBody MapConditionServicePackageRequest mapConditionServicePackageRequest) {
        ApiResponse response;
        List<ConditionRequest> listInput;
        List<MapConditionServicePackage> listResponse = new ArrayList<>();
        try {
            conditionService.deleteAllMap(mapConditionServicePackageRequest.getPackageId(), mapConditionServicePackageRequest.getProgramId());
            listInput = mapConditionServicePackageRequest.getListCondition();
            for(ConditionRequest input : listInput) {
                MapConditionServicePackage item = conditionService.saveCondition(input, mapConditionServicePackageRequest.getPackageId(), mapConditionServicePackageRequest.getProgramId());
                listResponse.add(item);
            }
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), ApiResponseStatus.SUCCESS.getValue());
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.SAVE_CONDITION_FAILED);
            logger.error("SAVE_CONDITION_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-condition")
    public ResponseEntity<?> getCondition(@RequestParam(name = "packageId") Long packageId, @RequestParam(name = "programId") Long programId) {
        ApiResponse response;
        List<MapConditionServicePackage> listResponse = new ArrayList<>();
        try {
            listResponse = conditionService.getCondition(packageId, programId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listResponse);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.CONDITION_NOT_LOADED);
            logger.error("GET_CONDITION_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

}
