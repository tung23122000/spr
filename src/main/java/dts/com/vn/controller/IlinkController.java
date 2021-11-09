package dts.com.vn.controller;

import com.google.gson.JsonObject;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.service.ConditionService;
import dts.com.vn.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IlinkController {

	private static final Logger logger = LoggerFactory.getLogger(IlinkController.class);

	private final ConditionService conditionService;

	@Autowired
	public IlinkController(ConditionService conditionService) {
		this.conditionService = conditionService;
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

}
