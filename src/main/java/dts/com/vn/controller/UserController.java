package dts.com.vn.controller;

import dts.com.vn.entities.Account;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.response.AccountResponse;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<ApiResponse> getAllUser() {
        ApiResponse response = null;
        try {
            List<Account> list = userService.getAllUser();
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.GET_ALL_USER_FAILED);
            logger.error("GET_ALL_USER_FAILED", response);
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
            response = new ApiResponse(ex, ErrorCode.EXIST_USER);
            logger.error("EXIST_USER", response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.ADD_USER_FAILED);
            logger.error("ADD_USER_FAILED", response);
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
            response = new ApiResponse(ex, ErrorCode.USER_NOT_FOUND);
            logger.error("USER_NOT_FOUND", response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.DATA_FAILED);
            logger.error("DATA_FAILED", response);
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
            response = new ApiResponse(ex, ErrorCode.USER_NOT_FOUND);
            logger.error("USER_NOT_FOUND", response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.UPDATE_USER_FAILED);
            logger.error("UPDATE_USER_FAILED", response);
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
            response = new ApiResponse(ex, ErrorCode.USER_NOT_FOUND);
            logger.error("USER_NOT_FOUND", response);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.DELETE_USER_FAILED);
            logger.error("DELETE_USER_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
