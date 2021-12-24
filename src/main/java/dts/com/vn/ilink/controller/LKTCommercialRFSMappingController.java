package dts.com.vn.ilink.controller;

import dts.com.vn.config.SpringFoxConfig;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.LogConstants;
import dts.com.vn.ilink.dto.CommercialMappingRequest;
import dts.com.vn.ilink.entities.CommercialMapping;
import dts.com.vn.ilink.service.CommercialRFSMappingService;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ServicePackageService;
import dts.com.vn.util.LogUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@Api(tags = {SpringFoxConfig.LKT_COMMERCIAL_RFS_MAPPING_TAG})
public class LKTCommercialRFSMappingController {

	private static final Logger logger = LoggerFactory.getLogger(LKTCommercialRFSMappingController.class);

	private final CommercialRFSMappingService commercialRFSMappingService;

	private final ServicePackageService servicePackageService;

	@Autowired
	public LKTCommercialRFSMappingController(CommercialRFSMappingService commercialRFSMappingService,
	                                         ServicePackageService servicePackageService) {
		this.commercialRFSMappingService = commercialRFSMappingService;
		this.servicePackageService = servicePackageService;
	}

	@ApiOperation(value = "Lấy danh sách các gói cước đã chuyển sang hệ thống flowone")
	@GetMapping("/find-all-flowone-package")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAllFOPackage() {
		ApiResponse response;
		try {
			response = servicePackageService.findAllFOPackage();
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response);
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
			LogUtil.writeLog(logger, LogConstants.ERROR, response);
			return ResponseEntity.badRequest().body(response);
		}
	}

	@ApiOperation(value = "Lấy danh sách mapping gói cước với luồng trong catalog")
	@GetMapping("/find-all-mapping")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAll(@RequestParam(required = false) String search,
	                                           Pageable pageable) {
		LogUtil.writeLog(logger, LogConstants.REQUEST, pageable);
		ApiResponse response;
		try {
			response = commercialRFSMappingService.findAll(search, pageable);
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response);
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
			LogUtil.writeLog(logger, LogConstants.ERROR, response);
			return ResponseEntity.badRequest().body(response);
		}
	}

	@ApiOperation(value = "Tạo mới 1 bản ghi mapping gói cước với luồng trong catalog")
	@PostMapping("/create-mapping")
	@ResponseBody
	public ResponseEntity<ApiResponse> createMapping(@RequestBody @Valid CommercialMappingRequest request) {
		LogUtil.writeLog(logger, LogConstants.REQUEST, request);
		ApiResponse response;
		try {
			response = commercialRFSMappingService.createMapping(request);
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response);
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getLocalizedMessage());
			LogUtil.writeLog(logger, LogConstants.ERROR, response);
			return ResponseEntity.badRequest().body(response);
		}
	}

	@ApiOperation(value = "Xóa 1 bản ghi mapping gói cước với luồng trong catalog")
	@DeleteMapping("/delete-mapping")
	@ResponseBody
	public ResponseEntity<ApiResponse> deleteMapping(@RequestBody @Valid CommercialMapping request) {
		ApiResponse response;
		try {
			response = commercialRFSMappingService.deleteMapping(request);
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			logger.error(ex.toString());
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
			return ResponseEntity.badRequest().body(response);
		}
	}

}
