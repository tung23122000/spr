package dts.com.vn.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import dts.com.vn.entities.Services;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ServicesService;

@RestController
@RequestMapping("/api/services")
public class ServicesController {

  @Autowired
  private ServicesService servicesService;

  @GetMapping("/get-all")
  public ResponseEntity<ApiResponse> getAll() {
    ApiResponse response;
    try {
      List<Services> services = servicesService.getAll();
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), services);
    } catch (RestApiException ex) {
      response = new ApiResponse(ex);
    } catch (Exception ex) {
      ex.printStackTrace();
      response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }
}
