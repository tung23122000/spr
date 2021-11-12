package dts.com.vn.ilink.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.service.ConditionService;
import dts.com.vn.request.ListConditionRequest;
import dts.com.vn.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class IlinkController {

	private static final Logger logger = LoggerFactory.getLogger(IlinkController.class);

	private final ConditionService conditionService;

	@Autowired
	public IlinkController(ConditionService conditionService) {
		this.conditionService = conditionService;
	}

	@GetMapping("/find-all-condition")
	public ResponseEntity<ApiResponse> findConditionByProgramCodeAndTransaction() {
		ApiResponse response;
		try {
			response = conditionService.findAllCondition();
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			logger.error(ex.toString());
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@GetMapping("/find-conditon-by-programCode")
	public ResponseEntity<ApiResponse> findConditionByProgramCodeAndTransaction(@RequestParam String programCode,
	                                                                            @RequestParam String transaction) {
		JsonObject requestJson = new JsonObject();
		requestJson.addProperty("programCode", programCode);
		requestJson.addProperty("transaction", transaction);
		logger.info("==========>   " + requestJson);
		ApiResponse response;
		try {
			response = conditionService.findConditionByProgramCodeAndTransaction(programCode, transaction);
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			logger.error(ex.toString());
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

	@PostMapping("/update-list-condition")
	public ResponseEntity<ApiResponse> updateListCondition(@RequestBody ListConditionRequest request) {
		logger.info("==========>   " + new Gson().toJson(request));
		ApiResponse response;
		try {
			response = conditionService.updateListCondition(request.getProgramCode(), request.getTransaction(), request.getListCondition());
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			logger.error(ex.toString());
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

}
