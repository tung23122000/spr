package dts.com.vn.controller;

import dts.com.vn.entities.*;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.IsdnListRepository;
import dts.com.vn.request.AddServiceProgramRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.ServiceProgramResponse;
import dts.com.vn.response.service_package_detail.DetailServicePackageResponse;
import dts.com.vn.response.service_package_detail.DetailServiceProgramResponse;
import dts.com.vn.service.IsdnListService;
import dts.com.vn.service.ListDetailService;
import dts.com.vn.service.ServicePackageListService;
import dts.com.vn.service.ServiceProgramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@RestController
@RequestMapping("/api/service-program")
public class ServiceProgramController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceProgramController.class);

	private final ServiceProgramService serviceProgramService;

	private final IsdnListService isdnListService;

	private final ListDetailService listDetailService;

	private final ServicePackageListService servicePackageListService;


	public ServiceProgramController(ServiceProgramService serviceProgramService, IsdnListService isdnListService, ListDetailService listDetailService, ServicePackageListService servicePackageListService) {
		this.serviceProgramService = serviceProgramService;
		this.isdnListService = isdnListService;
		this.listDetailService = listDetailService;
		this.servicePackageListService = servicePackageListService;
	}

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

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@PutMapping("/update")
	public ResponseEntity<ApiResponse> update(@RequestBody AddServiceProgramRequest request) {
		ApiResponse response;
		try {
			if (request.getFileName() != null && request.getListIsdn().size()>0){
				logger.info("Import isdn list: {}", request.getFileName(), request.getListIsdn());
				//			Create isdn_list
				IsdnList isdnListRequest = new IsdnList(null, request.getFileName(), Instant.now(), null, "1", null, "0");
				IsdnList isdnListResponse = isdnListService.save(isdnListRequest);
				//			Create List_detail
				Long isdnListId = isdnListResponse.getIsdnListId();
				List<String> listDetailInput = request.getListIsdn();
				for (String input : listDetailInput) {
					ListDetail listDetail = new ListDetail(null, isdnListResponse.getIsdnListId(), input.trim(), "0", 0L, 91);
					listDetailService.save(listDetail);
				}
				//			Create ServicePackageList
				ServicePackageList servicePackageList = new ServicePackageList(request.getServicePackageId(), isdnListResponse.getIsdnListId(), Instant.now(), null, request.getServiceProgramId(), null);
				servicePackageListService.save(servicePackageList);
			}

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
	public ResponseEntity<ApiResponse> findByPackageId(@PathVariable(name = "packageId") Long packageId, Pageable pageable) {
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

	@GetMapping("/get-all-actioncode-mapping")
	public ResponseEntity<ApiResponse> getAllActioncodeMapping() {
		ApiResponse response;
		try {
			List<ActioncodeMapping> list = serviceProgramService.getAllActioncodeMapping();
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
