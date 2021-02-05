package dts.com.vn.controller;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.AddServiceProgramRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.ServiceProgramResponse;
import dts.com.vn.response.service_package_detail.DetailServicePackageResponse;
import dts.com.vn.response.service_package_detail.DetailServiceProgramResponse;
import dts.com.vn.service.ServiceProgramService;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-program")
public class ServiceProgramController {

  @Autowired
  private ServiceProgramService serviceProgramService;

  @GetMapping("/find-all")
  public ResponseEntity<ApiResponse> findAll(
      @RequestParam(name = "search", required = false) String search, Pageable pageable) {
    ApiResponse response = null;
    try {
      Page<ServiceProgram> page = serviceProgramService.findAll(search, pageable);
      Page<ServiceProgramResponse> pageResponse =
          page.map(new Function<ServiceProgram, ServiceProgramResponse>() {
            @Override
            public ServiceProgramResponse apply(ServiceProgram service) {
              return new ServiceProgramResponse(service);
            }
          });
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), pageResponse);
    } catch (Exception e) {
      response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> add(@RequestBody AddServiceProgramRequest request) {
    ApiResponse response;
    try {
      ServiceProgram data = serviceProgramService.add(request);
      ServiceProgramResponse responseEntity = new ServiceProgramResponse(data);
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
  public ResponseEntity<ApiResponse> update(@RequestBody AddServiceProgramRequest request) {
    ApiResponse response;
    try {
      ServiceProgram data = serviceProgramService.update(request);
      ServiceProgramResponse responseEntity = new ServiceProgramResponse(data);
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
      ServiceProgram servicePackage = serviceProgramService.findById(id);
      ServiceProgramResponse responseEntity = new ServiceProgramResponse(servicePackage);
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
    } catch (RestApiException ex) {
      response = new ApiResponse(ex);
    } catch (Exception ex) {
      ex.printStackTrace();
      response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/find-by-package-id/{packageId}")
  public ResponseEntity<ApiResponse> findByPackageId(
      @PathVariable(name = "packageId", required = true) Long packageId, Pageable pageable) {
    ApiResponse response;
    try {
      DetailServicePackageResponse entity =
          serviceProgramService.findByPackageId(packageId, pageable);
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entity);
    } catch (RestApiException ex) {
      response = new ApiResponse(ex);
    } catch (Exception ex) {
      ex.printStackTrace();
      response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/detail-service-program/{programId}")
  public ResponseEntity<ApiResponse> detailServiceProgram(
      @PathVariable(name = "programId", required = true) Long programId,
      @Qualifier(value = "pageableIN") Pageable pageableIN,
      @Qualifier(value = "pageableBILLING") Pageable pageableBILLING,
      @Qualifier(value = "pageablePCRF") Pageable pageablePCRF,
      @Qualifier(value = "pageServiceInfo") Pageable pageServiceInfo) {
    ApiResponse response;
    try {
      DetailServiceProgramResponse entity = serviceProgramService.detailServiceProgram(programId,
          pageableIN, pageableBILLING, pageablePCRF, pageServiceInfo);
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entity);
    } catch (RestApiException ex) {
      response = new ApiResponse(ex);
    } catch (Exception ex) {
      ex.printStackTrace();
      response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }
}
