package dts.com.vn.controller;

import dts.com.vn.entities.IsdnList;
import dts.com.vn.entities.ListDetailNew;
import dts.com.vn.entities.ServicePackageList;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.repository.ListDetailNewRepository;
import dts.com.vn.request.ServicePackageListRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.IsdnListService;
import dts.com.vn.service.ServicePackageListService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/api/service-package-list")
public class ServicePackageListController {
    private static final Logger logger = LoggerFactory.getLogger(ServicePackageListController.class);

    private final ServicePackageListService servicePackageListService;
    private final IsdnListService isdnListService;
    private final ListDetailNewRepository listDetailNewRepository;

    public ServicePackageListController(ServicePackageListService servicePackageListService,
                                        IsdnListService isdnListService, ListDetailNewRepository listDetailNewRepository) {
        this.servicePackageListService = servicePackageListService;
        this.isdnListService = isdnListService;
        this.listDetailNewRepository = listDetailNewRepository;
    }

    @Transactional
    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(@RequestBody ServicePackageListRequest servicePackageListRequest) {
        ApiResponse response = null;
        try {
            servicePackageListService.save(servicePackageListRequest);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.ADD_SERVICE_PACKAGE_LIST_FAILED);
            logger.error("ADD_SERVICE_PACKAGE_LIST_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
