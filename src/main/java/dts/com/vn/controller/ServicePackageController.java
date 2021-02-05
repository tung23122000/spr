package dts.com.vn.controller;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.AddServicePackageRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.ServicePackageResponse;
import dts.com.vn.service.ServicePackageService;
  
@RestController
@RequestMapping("/api/service-package")
public class ServicePackageController {

  @Autowired
  private ServicePackageService servicePackageService;

  @GetMapping("/find-all")
  public ResponseEntity<ApiResponse> findAll(@RequestParam(name = "search", required = false) String search, Pageable pageable) {
    ApiResponse response;
    try {
      Page<ServicePackage> page = servicePackageService.findAll(search, pageable);
      Page<ServicePackageResponse> pageResponse =
          page.map(new Function<ServicePackage, ServicePackageResponse>() {
            @Override
            public ServicePackageResponse apply(ServicePackage t) {
              return new ServicePackageResponse(t);
            }
          });
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), pageResponse);
    } catch (RestApiException ex) {
      response = new ApiResponse(ex);
    } catch (Exception ex) {
      ex.printStackTrace();
      response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> add(@RequestBody AddServicePackageRequest request) {
    ApiResponse response;
    try {
      ServicePackage page = servicePackageService.add(request);
      ServicePackageResponse responseEntity = new ServicePackageResponse(page);
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
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
      ServicePackage servicePackage = servicePackageService.findById(id);
      ServicePackageResponse responseEntity = new ServicePackageResponse(servicePackage);
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
    } catch (RestApiException ex) {
      response = new ApiResponse(ex);
    } catch (Exception ex) {
      ex.printStackTrace();
      response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }

  @PutMapping("/update")
  public ResponseEntity<ApiResponse> update(@RequestBody AddServicePackageRequest request) {
    ApiResponse response;
    try {
      ServicePackage page = servicePackageService.update(request);
      ServicePackageResponse responseEntity = new ServicePackageResponse(page);
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
    } catch (RestApiException ex) {
      response = new ApiResponse(ex);
    } catch (Exception ex) {
      ex.printStackTrace();
      response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }
}