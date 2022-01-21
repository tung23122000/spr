package dts.com.vn.controller;

import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.enumeration.LogConstants;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.ConfigFlowConditionRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ConfigFlowConditionService;
import dts.com.vn.util.LogUtil;
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
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response);
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex);
		}
		return ResponseEntity.ok().body(response);
	}

	@Transactional
	@PostMapping("/create-flow")
	public ResponseEntity<ApiResponse> create(@RequestBody ConfigFlowConditionRequest request) {
		logger.info("=========> " + "ConfigRequest: " + request.toString());
		ApiResponse response;
		try {
			response = configFlowConditionService.save(request);
			logger.info("=========> " + "ConfigResponse: " + response.getData());
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.CONFIG_FLOW_CONDITION_ERROR);
			logger.error("CONFIG_FLOW_CONDITION_ERROR", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/delete")
	public ResponseEntity<ApiResponse> delete(@RequestParam Long id) {
		logger.info("=========> " + "ConditionId: " + id);
		ApiResponse response;
		try {
			response = configFlowConditionService.delete(id);
			logger.info("=========> " + "ConditionId: " + id);
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
		logger.info("=========> " + "ConfigRequest: " + flowConditionRequest);
		ApiResponse response;
		try {
			response = configFlowConditionService.update(flowConditionRequest);
			logger.info("=========> " + "Response: " + response.getData());
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.CONFIG_FLOW_CONDITION_UPDATE_ERROR);
			logger.error("CONFIG_FLOW_CONDITION_UPDATE_ERROR", response);
		}
		return ResponseEntity.ok().body(response);
	}


}
