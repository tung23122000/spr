package dts.com.vn.controller;

import dts.com.vn.entities.MapRolePermission;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.RolePermissionResponse;
import dts.com.vn.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("/getPermissionById/{id}")
    public ResponseEntity<ApiResponse> getPermissionById(@PathVariable(name = "id", required = true) Long id) {
        ApiResponse response = null;
        try {
            RolePermissionResponse list = roleService.getPermissionById(id);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
        }
        return ResponseEntity.ok().body(response);
    }
}