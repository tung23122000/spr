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
import dts.com.vn.entities.ServiceInfo;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.AddServiceInfoRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.ServiceInfoResponse;
import dts.com.vn.service.ServiceInfoService;

@RestController
@RequestMapping("/api/service-info")
public class ServiceInfoController {

  @Autowired
  private ServiceInfoService serviceInfoService;

  @GetMapping("/find-all")
  public ResponseEntity<ApiResponse> findAll(@RequestParam(name = "search", required = false) String search, Pageable pageable) {
    ApiResponse response;
    try {
      Page<ServiceInfo> page = serviceInfoService.findAll(search, pageable);
      Page<ServiceInfoResponse> pageRes =
          page.map(new Function<ServiceInfo, ServiceInfoResponse>() {

            @Override
            public ServiceInfoResponse apply(ServiceInfo t) {
              return new ServiceInfoResponse(t);
            }

          });
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), pageRes);
    } catch (Exception e) {
      response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/find-by-id/{id}")
  public ResponseEntity<ApiResponse> findById(@PathVariable("id") Long id) {
    ApiResponse response;
    try {
      ServiceInfo service = serviceInfoService.findById(id);
      ServiceInfoResponse entityRes = new ServiceInfoResponse(service);
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
    } catch (RestApiException ex) {
      response = new ApiResponse(ex);
    } catch (Exception e) {
      response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> add(@RequestBody AddServiceInfoRequest request) {
    ApiResponse response;
    try {
      ServiceInfo service = serviceInfoService.add(request);
      ServiceInfoResponse entityRes = new ServiceInfoResponse(service);
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
    } catch (RestApiException ex) {
      response = new ApiResponse(ex);
    } catch (Exception e) {
      response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }

  @PutMapping("/update")
  public ResponseEntity<ApiResponse> update(@RequestBody AddServiceInfoRequest request) {
    ApiResponse response;
    try {
      ServiceInfo service = serviceInfoService.update(request);
      ServiceInfoResponse entityRes = new ServiceInfoResponse(service);
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
    } catch (RestApiException ex) {
      response = new ApiResponse(ex);
    } catch (Exception e) {
      response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }
}
