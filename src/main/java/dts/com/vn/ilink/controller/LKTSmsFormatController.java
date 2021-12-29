package dts.com.vn.ilink.controller;

import dts.com.vn.config.SpringFoxConfig;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.LogConstants;
import dts.com.vn.ilink.dto.BstLookupTableRowRequestCustom;
import dts.com.vn.ilink.service.SmsFormatService;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.util.LogUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Api(tags = {SpringFoxConfig.LKT_SMS_FORMAT_TAG})
public class LKTSmsFormatController {

	private static final Logger logger = LoggerFactory.getLogger(LKTSmsFormatController.class);

	private final SmsFormatService smsFormatService;

	@Autowired
	public LKTSmsFormatController(SmsFormatService smsFormatService) {
		this.smsFormatService = smsFormatService;
	}

	@ApiOperation(value = "Lấy thông tin các bản ghi trong bảng LKT_SMS_FORMAT")
	@GetMapping("/find-all-fo-flow")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAllFlow() {
		ApiResponse response;
		try {
			response = smsFormatService.findAllFoFlow();
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response);
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
			LogUtil.writeLog(logger, LogConstants.ERROR, response);
			return ResponseEntity.badRequest().body(response);
		}
	}

	@ApiOperation(value = "Lấy thông tin các bản ghi trong bảng LKT_SMS_FORMAT")
	@GetMapping("/find-all-sms-format")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAll(@RequestParam(required = false) String search,
	                                           Pageable pageable) {
		if (StringUtils.isNotBlank(search)) LogUtil.writeLog(logger, LogConstants.REQUEST, search);
		LogUtil.writeLog(logger, LogConstants.REQUEST, pageable);
		ApiResponse response;
		try {
			response = smsFormatService.findAll(search, pageable);
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response);
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
			LogUtil.writeLog(logger, LogConstants.ERROR, response);
			return ResponseEntity.badRequest().body(response);
		}
	}

	@ApiOperation(value = "Tạo mới 1 bản ghi trong bảng LKT_SMS_FORMAT")
	@PostMapping("/create-sms")
	@ResponseBody
	public ResponseEntity<ApiResponse> createPackageInfo(@RequestBody BstLookupTableRowRequestCustom request) {
		LogUtil.writeLog(logger, LogConstants.REQUEST, request);
		ApiResponse response;
		try {
			response = smsFormatService.createSMS(request);
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response);
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getLocalizedMessage());
			LogUtil.writeLog(logger, LogConstants.ERROR, response);
			return ResponseEntity.ok().body(response);
		}
	}

	@ApiOperation(value = "Xóa 1 bản ghi trong bảng LKT_SMS_FORMAT")
	@PostMapping("/delete-sms")
	@ResponseBody
	public ResponseEntity<ApiResponse> deletePackageInfo(@RequestBody BstLookupTableRowRequestCustom request) {
		LogUtil.writeLog(logger, LogConstants.REQUEST, request);
		ApiResponse response;
		try {
			response = smsFormatService.deleteSMS(request);
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response);
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getLocalizedMessage());
			LogUtil.writeLog(logger, LogConstants.ERROR, response);
			return ResponseEntity.ok().body(response);
		}
	}


}
