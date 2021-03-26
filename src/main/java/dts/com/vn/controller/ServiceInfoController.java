package dts.com.vn.controller;

import dts.com.vn.entities.ServiceInfo;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.AddServiceInfoRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.ServiceInfoResponse;
import dts.com.vn.service.ServiceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/service-info")
public class ServiceInfoController {


	private final ServiceInfoService serviceInfoService;

	@Autowired
	public ServiceInfoController(ServiceInfoService serviceInfoService) {
		this.serviceInfoService = serviceInfoService;
	}

	@GetMapping("/find-all")
	public ResponseEntity<ApiResponse> findAll(@RequestParam(name = "search", required = false) String search, Pageable pageable) {
		ApiResponse response;
		try {
			Page<ServiceInfo> page = serviceInfoService.findAll(search, pageable);
			Page<ServiceInfoResponse> pageRes = page.map(ServiceInfoResponse::new);
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

	@GetMapping("/find-all-by-packageId")
	public ResponseEntity<ApiResponse> findAllByPackageId(@RequestParam(name = "packageId") Long packageId) {
		ApiResponse response;
		try {
			List<ServiceInfo> serviceInfos = serviceInfoService.findAllByPackageId(packageId);
			List<ServiceInfoResponse> data = serviceInfos.stream().map(ServiceInfoResponse::new).collect(Collectors.toList());
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), data);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

}
