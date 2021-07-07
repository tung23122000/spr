package dts.com.vn.controller;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.RolePermissionResponse;
import dts.com.vn.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/role")
public class RoleController {

	private final RoleService roleService;

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@Autowired
	public RoleController(RoleService roleService) {
		this.roleService = roleService;
	}

	@GetMapping("/getPermissionById/{id}")
	public ResponseEntity<ApiResponse> getPermissionById(@PathVariable(name = "id") Long id) {
		ApiResponse response;
		try {
			RolePermissionResponse list = roleService.getPermissionById(id);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.GET_PERMISSION_FAILED);
			logger.error("GET_PERMISSION_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

}
