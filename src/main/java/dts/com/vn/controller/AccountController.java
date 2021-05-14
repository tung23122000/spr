package dts.com.vn.controller;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.LoginRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AccountController {

	@Autowired
	private LoginService loginService;

	@PostMapping("/login")
	public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginReq,
	                                         HttpServletRequest httpRequest) {
		ApiResponse response;
		try {
			Map<String, Object> data = loginService.login(loginReq, httpRequest);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), data);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse> logout() {
		ApiResponse response;
		try {
			loginService.logout();
			response = ApiResponse.builder().status(ApiResponseStatus.SUCCESS.getValue()).build();
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}
}
