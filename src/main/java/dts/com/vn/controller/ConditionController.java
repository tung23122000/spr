package dts.com.vn.controller;

import dts.com.vn.entities.Condition;
import dts.com.vn.entities.MapConditionServicePackage;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.request.ConditionRequest;
import dts.com.vn.request.MapConditionServicePackageRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/condition")
public class ConditionController {

    @Autowired
    private ConditionService conditionService;

    @GetMapping("/find-all")
    public ResponseEntity<?> findAll() {
        ApiResponse response = null;
        try {
            List<Condition> list = conditionService.findAll();
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
        }
        return ResponseEntity.ok().body(response);
    }

    @Transactional(propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class)
    @PostMapping("/save-condition")
    public ResponseEntity<?> saveCondition(@RequestBody MapConditionServicePackageRequest mapConditionServicePackageRequest) {
        ApiResponse response = null;
        List<ConditionRequest> listInput = null;
        List<MapConditionServicePackage> listResponse = new ArrayList<>();
        try {
            listInput = mapConditionServicePackageRequest.getListCondition();
            for(ConditionRequest input : listInput) {
                MapConditionServicePackage item = conditionService.saveCondition(input, mapConditionServicePackageRequest.getPackageId(), mapConditionServicePackageRequest.getProgramId());
                listResponse.add(item);
            }
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listResponse);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
        }
        return ResponseEntity.ok().body(response);
    }
}
