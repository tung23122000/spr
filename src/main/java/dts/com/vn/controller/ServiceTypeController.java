package dts.com.vn.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ServiceTypeService;

@RestController
@RequestMapping("/api/service-type")
public class ServiceTypeController {

  @Autowired
  private ServiceTypeService serviceTypeService;

  @GetMapping("/get-list-active")
  public ResponseEntity<ApiResponse> getAllActive() {
    List<ServiceType> listData = serviceTypeService.findByStatusAndDisplayStatus();
    ApiResponse response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listData);
    return ResponseEntity.ok().body(response);
  }
}
