package dts.com.vn.controller;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.Constant;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.RenewDataRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.CustomQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/custom-query")
public class CustomQueryController {

	private static final Logger logger = LoggerFactory.getLogger(CustomQueryController.class);

	private final CustomQueryService customQueryService;

	@Autowired
	public CustomQueryController(CustomQueryService customQueryService) {
		this.customQueryService = customQueryService;
	}

	@PostMapping("/execute")
	public ResponseEntity<Object> executeQuery(@RequestBody RenewDataRequest renewDataRequest) {
		ApiResponse response = null;
		try {
			response = customQueryService.execute(renewDataRequest);
		} catch (RestApiException ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex);
		} catch (IOException ex) {

		}
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/upload")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
		logger.info("File upload " + file.getOriginalFilename());
		ApiResponse response;
		try {
			String fileName = customQueryService.storeFileToServer(file);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), fileName, null, Constant.UPLOAD_FILE_SUCCESS);
			logger.info(response.toString());
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", Constant.UPLOAD_FILE_FAIL);
			return ResponseEntity.badRequest().body(response);
		}
	}

}
