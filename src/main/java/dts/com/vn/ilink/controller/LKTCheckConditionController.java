package dts.com.vn.ilink.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dts.com.vn.config.SpringFoxConfig;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.service.ConditionService;
import dts.com.vn.request.ListConditionRequest;
import dts.com.vn.response.ApiResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api(tags = {SpringFoxConfig.LKT_CHECK_CONDITIONS_TAG})
public class LKTCheckConditionController {

	private static final Logger logger = LoggerFactory.getLogger(LKTCheckConditionController.class);

	private final ConditionService conditionService;

	@Autowired
	public LKTCheckConditionController(ConditionService conditionService) {
		this.conditionService = conditionService;
	}

	@ApiOperation(value = "Lấy danh sách các điều kiện của hệ thống")
	@GetMapping("/find-all-condition")
	@ResponseBody
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

	@ApiOperation(value = "Lấy danh sách điều kiện đang có của 1 chương trình theo 1 luồng")
	@GetMapping("/find-conditon-by-programCode")
	@ResponseBody
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

	@ApiOperation(value = "Thêm mới danh sách điều kiện của 1 chương trình theo 1 luồng")
	@PostMapping("/create-list-condition")
	@ResponseBody
	public ResponseEntity<ApiResponse> createListCondition(@RequestBody ListConditionRequest request) {
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

	@ApiOperation(value = "Cập nhật danh sách điều kiện của 1 chương trình theo 1 luồng")
	@PostMapping("/update-list-condition")
	@ResponseBody
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
