package dts.com.vn.ilink.controller;

import dts.com.vn.config.SpringFoxConfig;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.LogConstants;
import dts.com.vn.ilink.service.PackageInfoService;
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
@Api(tags = {SpringFoxConfig.LKT_PACKAGE_INFO_TAG})
public class LKTPackageInfoController {

	private static final Logger logger = LoggerFactory.getLogger(LKTCommercialRFSMappingController.class);

	private final PackageInfoService packageInfoService;

	@Autowired
	public LKTPackageInfoController(PackageInfoService packageInfoService) {
		this.packageInfoService = packageInfoService;
	}

	@ApiOperation(value = "Lấy thông tin cấu hình của các gói cước trong bảng LKT_PACKAGE_INFO")
	@GetMapping("/find-all-package-info")
	@ResponseBody
	public ResponseEntity<ApiResponse> findAll(@RequestParam(required = false) String search,
	                                           Pageable pageable) {
		if (StringUtils.isNotBlank(search)) LogUtil.writeLog(logger, LogConstants.REQUEST, search);
		LogUtil.writeLog(logger, LogConstants.REQUEST, pageable);
		ApiResponse response;
		try {
			response = packageInfoService.findAll(search, pageable);
			LogUtil.writeLog(logger, LogConstants.RESPONSE, response);
			return ResponseEntity.ok().body(response);
		} catch (Exception ex) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
			LogUtil.writeLog(logger, LogConstants.ERROR, response);
			return ResponseEntity.badRequest().body(response);
		}
	}


}
