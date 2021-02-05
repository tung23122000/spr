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
import dts.com.vn.entities.BucketsInfo;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.AddBucketsInfoRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.BucketsInfoResponse;
import dts.com.vn.service.BucketsInfoService;

@RestController
@RequestMapping("/api/buckets-info")
public class BucketsInfoController {

  @Autowired
  private BucketsInfoService bucketsInfoService;

  @GetMapping("/find-all")
  public ResponseEntity<ApiResponse> findAll(@RequestParam(name = "search", required = false) String search, Pageable pageable) {
    ApiResponse response = null;
    try {
      Page<BucketsInfo> page = bucketsInfoService.findAll(search, pageable);
      Page<BucketsInfoResponse> pageResponse =
          page.map(new Function<BucketsInfo, BucketsInfoResponse>() {
            @Override
            public BucketsInfoResponse apply(BucketsInfo service) {
              return new BucketsInfoResponse(service);
            }
          });
      response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), pageResponse);
    } catch (Exception e) {
      response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
    }
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> add(@RequestBody AddBucketsInfoRequest request) {
    ApiResponse response;
    try {
      BucketsInfo entity = bucketsInfoService.add(request);
      BucketsInfoResponse responseEntity = new BucketsInfoResponse(entity);
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
  public ResponseEntity<ApiResponse> update(@RequestBody AddBucketsInfoRequest request) {
    ApiResponse response;
    try {
      BucketsInfo entity = bucketsInfoService.update(request);
      BucketsInfoResponse responseEntity = new BucketsInfoResponse(entity);
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
      BucketsInfo entity = bucketsInfoService.findById(id);
      BucketsInfoResponse responseEntity = new BucketsInfoResponse(entity);
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
