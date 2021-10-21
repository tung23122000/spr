package dts.com.vn.controller;

import dts.com.vn.entities.MapConditionProgram;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.request.MapConditionProgramRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.MapConditionProgramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/condition-program")
public class MapConditionProgramController {
    private static final Logger logger = LoggerFactory.getLogger(MapConditionProgramController.class);

    private final MapConditionProgramService mapConditionProgramService;

    public MapConditionProgramController(MapConditionProgramService mapConditionProgramService) {
        this.mapConditionProgramService = mapConditionProgramService;
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(@RequestBody MapConditionProgramRequest mapConditionProgramRequest) {
        ApiResponse response = null;
        try {
            MapConditionProgram mapConditionProgram = mapConditionProgramService.save(mapConditionProgramRequest);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), mapConditionProgram);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.SAVE_CONDITION_PROGRAM_FAIL);
            logger.error("SAVE_CONDITION_PROGRAM_FAIL", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-one")
    public ResponseEntity<ApiResponse> getOne(@RequestParam Long programId, @RequestParam Integer conditionId) {
        ApiResponse response = null;
        try {
            MapConditionProgram mapConditionProgram = mapConditionProgramService.getOne(programId, conditionId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), mapConditionProgram);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.SAVE_CONDITION_PROGRAM_FAIL);
            logger.error("SAVE_CONDITION_PROGRAM_FAIL", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
