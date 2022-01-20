package dts.com.vn.controller;

import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.ConfigFlowConditionRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ConfigFlowConditionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @author BinhDT
 * @version 1.0.0
 * @created 20/01/2022
 */
@RestController
@RequestMapping("/api/config")
public class ConfigFlowConditionController {

	private static final Logger logger = LoggerFactory.getLogger(ConfigFlowConditionController.class);

	@Autowired
	private ConfigFlowConditionService configFlowConditionService;


	@GetMapping("/find-all")
	public ResponseEntity<ApiResponse> findAll() {
		ApiResponse response;
		try {
			response = configFlowConditionService.findAllConditon();
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex);
		}
		return ResponseEntity.ok().body(response);
	}

	@Transactional
	@PostMapping("/create-flow")
	public ResponseEntity<ApiResponse> create(@RequestBody ConfigFlowConditionRequest request) {

		ApiResponse response;
		try {
			response = configFlowConditionService.save(request);
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.CONFIG_FLOW_CONDITION_ERROR);
			logger.error("CONFIG_FLOW_CONDITION_ERROR", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/delete")
	public ResponseEntity<ApiResponse> delete(@RequestParam Long id) {
		ApiResponse response;
		try {
			response = configFlowConditionService.delete(id);
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.CONFIG_FLOW_CONDITION_DELETE_ERROR);
			logger.error("CONFIG_FLOW_CONDITION_DELETE_ERROR", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@Transactional
	@PostMapping("/update")
	public ResponseEntity<ApiResponse> update(@RequestBody ConfigFlowConditionRequest flowConditionRequest) {

		ApiResponse response;
		try {
			response = configFlowConditionService.update(flowConditionRequest);
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.CONFIG_FLOW_CONDITION_UPDATE_ERROR);
			logger.error("CONFIG_FLOW_CONDITION_UPDATE_ERROR", response);
		}
		return ResponseEntity.ok().body(response);
	}


}
