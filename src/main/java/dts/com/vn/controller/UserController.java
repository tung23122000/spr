package dts.com.vn.controller;

import dts.com.vn.entities.Account;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.response.AccountResponse;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/getAllUser")
	public ResponseEntity<ApiResponse> getAllUser() {
		ApiResponse response = null;
		try {
			List<Account> list = userService.getAllUser();
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse> add(@RequestBody Account request) {
		ApiResponse response;
		try {
			Account account = userService.add(request);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), account);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/find-by-id/{id}")
	public ResponseEntity<ApiResponse> findById(@PathVariable(name = "id", required = true) Long id) {
		ApiResponse response;
		try {
			Account entity = userService.findById(id);
			AccountResponse accountResponse = new AccountResponse(entity);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), accountResponse);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/update/{id}")
	public ResponseEntity<ApiResponse> add(@PathVariable Long id, @RequestBody Account request) {
		ApiResponse response;
		try {
			Account account = userService.update(id, request);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), account);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/delete/{id}")
	public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
		ApiResponse response;
		try {
			Account account = userService.delete(id);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), account);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}
}
