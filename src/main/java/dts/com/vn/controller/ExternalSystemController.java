package dts.com.vn.controller;

import dts.com.vn.entities.ExternalSystem;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.ExternalSystemResponse;
import dts.com.vn.service.ExternalSystemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/external-system")
public class ExternalSystemController {

	@Autowired
	private ExternalSystemService externalSystemService;

	private static final Logger logger = LoggerFactory.getLogger(ExternalSystemController.class);

	@GetMapping("/get-all-active")
	public ResponseEntity<ApiResponse> getAll() {
		ApiResponse response;
		try {
			List<ExternalSystem> list = externalSystemService.getData();
			List<ExternalSystemResponse> listEntity =
					list.stream().map((e) -> new ExternalSystemResponse(e)).collect(Collectors.toList());
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listEntity);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.DATA_FAILED);
			logger.error("CONVERT_DATA_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}
}
