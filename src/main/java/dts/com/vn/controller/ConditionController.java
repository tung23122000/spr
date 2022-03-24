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

@RestController
@RequestMapping("/api/condition")
public class ConditionController {

	private static final Logger logger = LoggerFactory.getLogger(ConditionController.class);

	private final ConditionService conditionService;

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

	@PostMapping("/save-condition")
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public ResponseEntity<?> saveCondition(@RequestBody MapConditionServicePackageRequest request) {
		ApiResponse response;
		List<ConditionRequest> listInput;
		try {
			// Xoá toàn bộ điều kiện cũ của chương trình
			conditionService.deleteAllMap(request.getPackageId(), request.getProgramId());
			listInput = request.getListCondition();
			for (ConditionRequest input : listInput) {
				conditionService.saveCondition(input, request.getPackageId(), request.getProgramId());
			}
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), "Thêm mới điều kiện cho chương trình thành công");
			logger.info(response.toString());
		} catch (Exception e) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), e.getLocalizedMessage());
			logger.error(response.toString());
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
