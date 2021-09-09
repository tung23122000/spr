package dts.com.vn.controller;

import dts.com.vn.config.FileStorageConfig;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.Constant;
import dts.com.vn.request.SQLRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.FileResponse;
import dts.com.vn.service.CustomQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/custom-query")
public class CustomQueryController {

	private static final Logger logger = LoggerFactory.getLogger(CustomQueryController.class);

	private final CustomQueryService customQueryService;

	private final FileStorageConfig fileStorageConfig;

	@Autowired
	public CustomQueryController(CustomQueryService customQueryService, FileStorageConfig fileStorageConfig) {
		this.customQueryService = customQueryService;
		this.fileStorageConfig = fileStorageConfig;
	}

	@PostMapping("/execute")
	public ResponseEntity<Object> executeQuery(@RequestBody SQLRequest sqlRequest) {
		ApiResponse response;
		try {
			response = customQueryService.excuteQuery(sqlRequest.getQuery());
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			logger.error(ex.toString());
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", Constant.UPLOAD_FILE_FAIL);
			return ResponseEntity.badRequest().body(response);
		}
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

	@GetMapping("/get-files")
	public ResponseEntity<?> getAllFiles() {
		ApiResponse response;
		try {
			List<FileResponse> result = customQueryService.getAllFiles(fileStorageConfig);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), result, null, "Lấy thông tin thành công");
			logger.info(response.toString());
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			logger.error(e.getMessage());
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", "Lấy thông tin thất bại");
			return ResponseEntity.badRequest().body(response);
		}
	}

}
