package dts.com.vn.controller;

import dts.com.vn.entities.LogAction;
import dts.com.vn.entities.NdsTypeParamProgram;
import dts.com.vn.entities.ServiceInfo;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.AddServiceInfoRequest;
import dts.com.vn.request.NdsTypeParamProgramRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.ServiceInfoResponse;
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.service.LogActionService;
import dts.com.vn.service.ServiceInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/service-info")
public class ServiceInfoController {
	private static final Logger logger = LoggerFactory.getLogger(ServiceInfoController.class);

	private final ServiceInfoService serviceInfoService;
	private final TokenProvider tokenProvider;
	private final LogActionService logActionService;

	public ServiceInfoController(ServiceInfoService serviceInfoService, TokenProvider tokenProvider, LogActionService logActionService) {
		this.serviceInfoService = serviceInfoService;
		this.tokenProvider = tokenProvider;
		this.logActionService = logActionService;
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

	@Transactional
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> add(@RequestBody AddServiceInfoRequest request) {
		ApiResponse response;
		try {
			ServiceInfo service = serviceInfoService.add(request);
			// Tạo Log Action
			LogAction logAction = new LogAction();
			logAction.setTableAction("service_info");
			logAction.setAccount(tokenProvider.account);
			logAction.setAction("CREATE");
			logAction.setOldValue(null);
			logAction.setIdAction(service.getServiceInfoId());
			logAction.setNewValue(service.toString());
			logAction.setTimeAction(new Date());
			logActionService.add(logAction);
			//
			ServiceInfoResponse entityRes = new ServiceInfoResponse(service);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@Transactional
	@PutMapping("/update")
	public ResponseEntity<ApiResponse> update(@RequestBody AddServiceInfoRequest request) {
		ApiResponse response;
		try {
			// Tạo Log Action
			LogAction logAction = new LogAction();
			logAction.setTableAction("service_info");
			logAction.setAccount(tokenProvider.account);
			logAction.setAction("UPDATE");
			logAction.setOldValue(serviceInfoService.findById(request.getServiceInfoId()).toString());
			// UPDATE
			ServiceInfo service = serviceInfoService.update(request);
			// Sau khi update set New Value
			logAction.setIdAction(service.getServiceInfoId());
			logAction.setNewValue(service.toString());
			logAction.setTimeAction(new Date());
			logActionService.add(logAction);
			ServiceInfoResponse entityRes = new ServiceInfoResponse(service);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/find-all-by-packageId-programId")
	public ResponseEntity<ApiResponse> findAllByPackageId(@RequestParam(name = "packageId") Long packageId, @RequestParam(name = "programId") Long programId) {
		ApiResponse response;
		try {
			List<ServiceInfo> serviceInfos = serviceInfoService.findAllByPackageId(packageId, programId);
			List<ServiceInfoResponse> data = serviceInfos.stream().map(ServiceInfoResponse::new).collect(Collectors.toList());
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), data);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@Transactional
	@PostMapping("/delete")
	public ResponseEntity<ApiResponse> delete(@RequestBody AddServiceInfoRequest addServiceInfoRequest){
		ApiResponse response = null;
		try {
			ServiceInfo serviceInfo = serviceInfoService.findById(addServiceInfoRequest.getServiceInfoId());
			// Tạo Log Action
			LogAction logAction = new LogAction();
			logAction.setTableAction("service_info");
			logAction.setAccount(tokenProvider.account);
			logAction.setAction("DELETE");
			logAction.setOldValue(serviceInfo.toString());
			logAction.setNewValue(null);
			logAction.setTimeAction(new Date());
			logAction.setIdAction(serviceInfo.getServiceInfoId());
			logActionService.add(logAction);
			serviceInfoService.delete(serviceInfo.getServiceInfoId());
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.DELETE_SERVICE_INFO_FAILED);
			logger.error("DELETE_SERVICE_INFO_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

}
