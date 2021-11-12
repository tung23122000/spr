package dts.com.vn.controller;

import dts.com.vn.entities.BucketsInfo;
import dts.com.vn.entities.LogAction;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.AddServicePackageRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.ServicePackageResponse;
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.service.LogActionService;
import dts.com.vn.service.ServicePackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Service;
import java.util.*;
import java.util.function.Function;

@RestController
@RequestMapping("/api/service-package")
public class ServicePackageController {

    @Autowired
    private ServicePackageService servicePackageService;

    @Autowired
    private LogActionService logActionService;

    @Autowired
    private TokenProvider tokenProvider;

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
            ServicePackage servicePackage = servicePackageService.add(request);
            // Tạo Log Action
            LogAction logAction = new LogAction();
            logAction.setTableAction("service_package");
            logAction.setAccount(tokenProvider.account);
            logAction.setAction("CREATE");
            logAction.setOldValue(null);
            logAction.setNewValue(servicePackage.toString());
            logAction.setTimeAction(new Date());
            logAction.setIdAction(servicePackage.getPackageId());
            logActionService.add(logAction);
            //		CREATE SUB_SERVICE_PACKAGE
            servicePackageService.saveSubServicePackge(request.getSubServicePackage(), servicePackage.getPackageId());
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
            // Tạo log action
            LogAction logAction = new LogAction();
            logAction.setTableAction("service_package");
            logAction.setAccount(tokenProvider.account);
            logAction.setAction("UPDATE");
            logAction.setOldValue(servicePackageService.findById(request.getServicePackageId()).toString());
            // UPDATE
            ServicePackage servicePackage = servicePackageService.update(request);
            // Sau khi update set New Value
            logAction.setIdAction(servicePackage.getPackageId());
            logAction.setNewValue(servicePackage.toString());
            logAction.setTimeAction(new Date());
            logActionService.add(logAction);

            ServicePackageResponse responseEntity = new ServicePackageResponse(servicePackage);
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

    // Chặn IN không cùng nhóm
    @GetMapping("/find-block-in-without-service-type/{packageId}")
    public ResponseEntity<ApiResponse> findBlockINWithoutServiceType(@PathVariable(name = "packageId", required = true) Long packageId) {
        ApiResponse response;
        List<ServicePackage> returnList;
        try {
            returnList = servicePackageService.findBlockINWithoutServiceType(packageId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), returnList);
        } catch (RestApiException ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
        }
        return ResponseEntity.ok().body(response);
    }

    // Chặn IN cùng nhóm
    @GetMapping("/find-block-in-with-service-type/{packageId}")
    public ResponseEntity<ApiResponse> findBlockINWithServiceType(@PathVariable(name = "packageId", required = true) Long packageId) {
        ApiResponse response;
        List<ServicePackage> returnList;
        try {
            returnList = servicePackageService.findBlockINWithServiceType(packageId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), returnList);
        } catch (RestApiException ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
        }
        return ResponseEntity.ok().body(response);
    }

    // Chặn PCRF không cùng nhóm
    @GetMapping("/find-block-pcrf-without-service-type/{packageId}")
    public ResponseEntity<ApiResponse> findBlockPCRFWithoutServiceType(@PathVariable(name = "packageId", required = true) Long packageId) {
        ApiResponse response;
        try {
            HashSet<ServicePackage> listBlockPCRF = servicePackageService.findBlockPCRFWithoutServiceType(packageId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listBlockPCRF);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
        }
        return ResponseEntity.ok().body(response);
    }

    // Chặn PCRF cùng nhóm
    @GetMapping("/find-block-pcrf-with-service-type/{packageId}")
    public ResponseEntity<ApiResponse> findBlockPCRFWithServiceType(@PathVariable(name = "packageId", required = true) Long packageId) {
        ApiResponse response;
        try {
            HashSet<ServicePackage> listBlockPCRF = servicePackageService.findBlockPCRFWithServiceType(packageId);
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

    @Transactional
    @PostMapping("/clone-service-package")
    public ResponseEntity<ApiResponse> cloneServicePackage(@RequestBody AddServicePackageRequest request) {
        ApiResponse response;
        try {
            response = servicePackageService.cloneServicePackage(request);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), "Exception " + e.getMessage(),
                    ErrorCode.CLONE_SERVICE_PACKAGE_FAILED.getErrorCode(),
                    ErrorCode.CLONE_SERVICE_PACKAGE_FAILED.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Transactional
    @PostMapping("/delete-service-package")
    public ResponseEntity<ApiResponse> deleteServicePackage(@RequestBody AddServicePackageRequest request) {
        ApiResponse response;
        try {
            ServicePackage servicePackage = servicePackageService.findById(request.getServicePackageId());
            servicePackageService.delete(servicePackage);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.DELETE_SERVICE_PACKAGE_FAILED);
            logger.error("DELETE_SERVICE_PACKAGE_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/switch-service-package/{packageId}")
    public ResponseEntity<ApiResponse> switchServicePackage(@PathVariable(name = "packageId") Long packageId) {
        ApiResponse response;
        try {
            servicePackageService.switchServicePackage(packageId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.API_FAILED_UNKNOWN);
        }
        return ResponseEntity.ok().body(response);
    }
}

