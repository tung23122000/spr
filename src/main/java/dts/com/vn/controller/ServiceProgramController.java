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
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.service.*;
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
	private final ServicePackageService servicePackageService;
	private final IsdnListService isdnListService;
	private final ListDetailService listDetailService;
	private final ServicePackageListService servicePackageListService;
	private final TokenProvider tokenProvider;
	private final LogActionService logActionService;

	public ServiceProgramController(ServiceProgramService serviceProgramService, IsdnListService isdnListService,
			ListDetailService listDetailService, ServicePackageListService servicePackageListService,
			ServicePackageService servicePackageService, TokenProvider tokenProvider, LogActionService logActionService) {
		this.serviceProgramService = serviceProgramService;
		this.isdnListService = isdnListService;
		this.listDetailService = listDetailService;
		this.servicePackageListService = servicePackageListService;
		this.servicePackageService = servicePackageService;
		this.tokenProvider = tokenProvider;
		this.logActionService = logActionService;
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
			response = new ApiResponse(e, ErrorCode.DATA_FAILED);
			logger.error("DATA_CONVERT_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse> add(@RequestBody AddServiceProgramRequest request) {
		ApiResponse response;
		try {
			ServiceProgram data = serviceProgramService.add(request);
			// Tạo Log Action
			LogAction logAction = new LogAction();
			logAction.setTableAction("service_program");
			logAction.setAccount(tokenProvider.account);
			logAction.setAction("CREATE");
			logAction.setOldValue(null);
			logAction.setNewValue(data.toString());
			logAction.setTimeAction(new Date());
			logAction.setIdAction(data.getProgramId());
			logActionService.add(logAction);
			//
			ServiceProgramResponse responseEntity = new ServiceProgramResponse(data);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
			logger.error("ADD_SERVICE_PROGRAM_FAILED", response);
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

//			Tạo log action
			LogAction logAction = new LogAction();
			logAction.setTableAction("service_program");
			logAction.setAccount(tokenProvider.account);
			logAction.setAction("UPDATE");
			logAction.setOldValue(serviceProgramService.findById(request.getServiceProgramId()).toString());

			ServiceProgram data = serviceProgramService.update(request);

			// Sau khi update set New Value
			logAction.setIdAction(data.getProgramId());
			logAction.setNewValue(data.toString());
			logAction.setTimeAction(new Date());
			logActionService.add(logAction);

			ServiceProgramResponse responseEntity = new ServiceProgramResponse(data);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
			logger.error("UPDATE_SERVICE_PROGRAM_FAILED", response);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
			logger.error("UPDATE_SERVICE_PROGRAM_FAILED", response);
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
			logger.error("FIND_BY_ID_SERVICE_PROGRAM_FAILED", response);
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
			logger.error("SERVICE_PACKAGE_NOT_FOUND", response);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
			logger.error("FIND_BY_PACKAGE_ID_NOT_FOUND", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/detail-service-program/{programId}")
	public ResponseEntity<ApiResponse> detailServiceProgram(
			@PathVariable(name = "programId", required = true) Long programId,
			@Qualifier(value = "pageableIN") Pageable pageableIN,
			@Qualifier(value = "pageableBILLING") Pageable pageableBILLING,
			@Qualifier(value = "pageablePCRF") Pageable pageablePCRF,
			@Qualifier(value = "pageableTransaction") Pageable pageableTransaction,
			@Qualifier(value = "pageServiceInfo") Pageable pageServiceInfo) {
		ApiResponse response;
		try {
			DetailServiceProgramResponse entity = serviceProgramService.detailServiceProgram(programId,
					pageableIN, pageableBILLING, pageablePCRF, pageableTransaction, pageServiceInfo);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entity);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
			logger.error("DETAIL_SERVICE_PROGRAM", response);
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
			logger.error("GET_ALL_ACTION_CODE_MAPPING", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/clone-program")
	public ResponseEntity<ApiResponse> cloneProgram(@RequestBody AddServiceProgramRequest request) {
		ApiResponse response;
		try {
			response = serviceProgramService.cloneOneServiceProgram(request.getServicePackageId(),
					request.getServicePackageId(), servicePackageService.findById(request.getServicePackageId()), serviceProgramService.findById(request.getServiceProgramId()));
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.CLONE_SERVICE_PROGRAM_FAILED);
			logger.error("CLONE_SERVICE_PROGRAM_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@Transactional
	@PostMapping("/delete-program")
	public ResponseEntity<ApiResponse> deleteServiceProgram(@RequestBody AddServiceProgramRequest request) {
		ApiResponse response;
		try {
			ServiceProgram serviceProgram = serviceProgramService.findById(request.getServiceProgramId());
			serviceProgramService.delete(serviceProgram);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
			logger.error("ADD_SERVICE_PROGRAM_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}
}
