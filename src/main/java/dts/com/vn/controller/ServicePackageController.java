package dts.com.vn.controller;

import dts.com.vn.entities.BucketsInfo;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.SubServicePackage;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.SubServicePackageRepository;
import dts.com.vn.request.AddServicePackageRequest;
import dts.com.vn.request.SubServicePackageRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.ServicePackageResponse;
import dts.com.vn.service.ServicePackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@RestController
@RequestMapping("/api/service-package")
public class ServicePackageController {

	@Autowired
	private ServicePackageService servicePackageService;

	@Autowired
	private SubServicePackageRepository subServicePackageRepository;

	private static final Logger logger = LoggerFactory.getLogger(ServicePackageController.class);

	@GetMapping("/find-all")
	public ResponseEntity<ApiResponse> findAll(
			@RequestParam(name = "search", required = false) String search,
			@RequestParam(name = "serviceTypeId", required = false) Long serviceTypeId, Pageable pageable) {
		ApiResponse response;
		try {
			Page<ServicePackage> page = servicePackageService.findAll(search, serviceTypeId, pageable);
			Page<ServicePackageResponse> pageResponse = page.map(ServicePackageResponse::new);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), pageResponse);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.DATA_FAILED);
			logger.error("DATA_SERVICE_PACKAGE_CONVERT_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@Transactional
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> add(@RequestBody AddServicePackageRequest request) {
		ApiResponse response;
		try {
			ServicePackage page = servicePackageService.add(request);
			//		CREATE SUB_SERVICE_PACKAGE
			List<SubServicePackageRequest> listBlock = request.getSubServicePackage();
			if (listBlock.size()>0){
				for (SubServicePackageRequest block: listBlock) {
					SubServicePackage subServicePackage = new SubServicePackage();
					subServicePackage.setPackageId(page.getPackageId());
					subServicePackage.setSubPackageId(block.getPackageId());
					subServicePackage.setIsActive("1");
					subServicePackageRepository.save(subServicePackage);
				}
			}
			ServicePackageResponse responseEntity = new ServicePackageResponse(page);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.DATA_FAILED);
			logger.error("DATA_SERVICE_PACKAGE_CONVERT_FAILED", response);
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
			response = new ApiResponse(ex, ErrorCode.DATA_FAILED);
			logger.error("DATA_SERVICE_PACKAGE_CONVERT_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/pending/{id}")
	public ResponseEntity<ApiResponse> pending(@PathVariable(name = "id", required = true) Long id) {
		ApiResponse response;
		try {
			ServicePackage servicePackage = servicePackageService.pending(id);
			ServicePackageResponse responseEntity = new ServicePackageResponse(servicePackage);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.DATA_FAILED);
			logger.error("PENDING_SERVICE_PACKAGE", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/active/{id}")
	public ResponseEntity<ApiResponse> active(@PathVariable(name = "id", required = true) Long id) {
		ApiResponse response;
		try {
			ServicePackage servicePackage = servicePackageService.active(id);
			ServicePackageResponse responseEntity = new ServicePackageResponse(servicePackage);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), responseEntity);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.DATA_FAILED);
			logger.error("ACTIVE_SERVICE_PACKAGE", response);
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
			response = new ApiResponse(ex, ErrorCode.DATA_FAILED);
			logger.error("UPDATE_SERVICE_PACKAGE", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/find-by-code/{code}")
	public ResponseEntity<ApiResponse> findByCode(
			@PathVariable(name = "code", required = true) String code) {
		ApiResponse response;
		try {
			ServicePackage servicePackage = servicePackageService.findByCode(code);
			if (Objects.nonNull(servicePackage)) {
				ServicePackageResponse responseEntity = new ServicePackageResponse(servicePackage);
				response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), responseEntity);
			} else {
				response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
			}
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/find-by-code-and-ignore-package-id/{code}/{packageId}")
	public ResponseEntity<ApiResponse> findByCodeIgnoreId(
			@PathVariable(name = "code", required = true) String code,
			@PathVariable(name = "packageId", required = true) Long packageId) {
		ApiResponse response;
		try {
			ServicePackage servicePackage = servicePackageService.findByCodeIgnoreId(code, packageId);
			if (Objects.nonNull(servicePackage)) {
				ServicePackageResponse responseEntity = new ServicePackageResponse(servicePackage);
				response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), responseEntity);
			} else {
				response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
			}
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/find-all-by-group-code-and-service-type-id")
	public ResponseEntity<ApiResponse> findAllByGroupCodeAndServiceTypeId(
			@RequestParam(name = "groupCode", required = false) String groupCode,
			@RequestParam(name = "serviceTypeId", required = false) Long serviceTypeId, Pageable pageable) {
		ApiResponse response;
		try {
			Page<ServicePackage> page = servicePackageService.findAllByGroupCodeAndServiceTypeId(groupCode, serviceTypeId, pageable);
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

	@GetMapping("/find-block-in/{id}")
	public ResponseEntity<ApiResponse> findBlockIN(@PathVariable(name = "id", required = true) Long id){
		ApiResponse response ;
		List<ServicePackage> returnList = new ArrayList<>();
		try {
//			SEARCH IN CỦA CHƯƠNG TRÌNH DEFAULT VỚI ID
			List<BucketsInfo> listBucketsInfo = servicePackageService.findBucketsInfo(id);
//			SEARCH LIST BLOCK BY BUCKETS INFO
			for (BucketsInfo bucketInfo: listBucketsInfo) {
				List<ServicePackage> listBlockIN = servicePackageService.findBlockIN(id, bucketInfo);
				returnList.addAll(listBlockIN);
			}
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), returnList);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/find-block-pcrf/{id}")
	public ResponseEntity<ApiResponse> findBlockPCRF(@PathVariable(name = "id", required = true) Long id){
		ApiResponse response ;
		List<ServicePackage> returnList = new ArrayList<>();
		try {
			List<ServicePackage> listBlockPCRF = servicePackageService.findBlockPCRF(id);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listBlockPCRF);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/get-all-without-pageable")
	public ResponseEntity<ApiResponse> getAllWithoutPageable() {
		ApiResponse response;
		try {
			List<ServicePackage> list = servicePackageService.getAllWithoutPageable();
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.DATA_FAILED);
			logger.error("DATA_SERVICE_PACKAGE_CONVERT_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/clone-service-package")
	public ResponseEntity<ApiResponse> cloneServicePackage(@RequestBody AddServicePackageRequest request) {
		ApiResponse response;
		try {
			response = servicePackageService.cloneServicePackage(request);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.DATA_FAILED);
			return ResponseEntity.badRequest().body(response);
		}
	}

}

