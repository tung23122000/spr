package dts.com.vn.controller;

import dts.com.vn.enumeration.ApiResponseStatus;
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

	private final ConfigFlowConditionService configFlowConditionServiceImpl;

	@Autowired
	public ConfigFlowConditionController(ConfigFlowConditionService configFlowConditionServiceImpl) {
		this.configFlowConditionServiceImpl = configFlowConditionServiceImpl;
	}

	@GetMapping("/find-all")
	public ResponseEntity<ApiResponse> findAll() {
		ApiResponse response;
		try {
			response = configFlowConditionServiceImpl.findAllConditon();
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
		LogUtil.writeLog(logger, LogConstants.REQUEST, request.toString());
		ApiResponse response;
		try {
			response = configFlowConditionServiceImpl.save(request);
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response.getData());
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.CONFIG_FLOW_CONDITION_ERROR);
			LogUtil.writeLog(logger, LogConstants.ERROR, ex);
		}
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/delete")
	public ResponseEntity<ApiResponse> delete(@RequestParam Long id) {
		LogUtil.writeLog(logger, LogConstants.REQUEST, id);
		ApiResponse response;
		try {
			response = configFlowConditionServiceImpl.delete(id);
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response.getData());
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.CONFIG_FLOW_CONDITION_DELETE_ERROR);
			LogUtil.writeLog(logger, LogConstants.ERROR, ex);
		}
		return ResponseEntity.ok().body(response);
	}

	@Transactional
	@PostMapping("/update")
	public ResponseEntity<ApiResponse> update(@RequestBody ConfigFlowConditionRequest flowConditionRequest) {
		LogUtil.writeLog(logger, LogConstants.REQUEST, flowConditionRequest.toString());
		ApiResponse response;
		try {
			response = configFlowConditionServiceImpl.update(flowConditionRequest);
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response.getData());
		} catch (Exception ex) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getLocalizedMessage());
			LogUtil.writeLog(logger, LogConstants.ERROR, response);
			return ResponseEntity.ok().body(response);
		}
		return ResponseEntity.ok().body(response);
	}


}
