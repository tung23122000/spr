package dts.com.vn.controller;

import dts.com.vn.entities.IsdnList;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.request.MapIsdnListRequest;
import dts.com.vn.request.PackageListRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ServicePackageListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/package-list")
public class PackageListController {
    private static final Logger logger = LoggerFactory.getLogger(PackageListController.class);

    private final ServicePackageListService servicePackageListService;

    public PackageListController(ServicePackageListService servicePackageListService) {
        this.servicePackageListService = servicePackageListService;
    }

    @PostMapping("/save")
    public ResponseEntity<ApiResponse> save(@RequestBody PackageListRequest packageListRequest) {
        ApiResponse response = null;
        try {
            servicePackageListService.save(packageListRequest);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.ADD_SERVICE_PACKAGE_LIST_FAILED);
            logger.error("ADD_SERVICE_PACKAGE_LIST_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/map-isdn-list")
    public ResponseEntity<ApiResponse> mapIsdnList(@RequestBody MapIsdnListRequest request) {
        ApiResponse response = null;
        try {
            servicePackageListService.mapIsdnList(request);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.ADD_SERVICE_PACKAGE_LIST_FAILED);
            logger.error("ADD_SERVICE_PACKAGE_LIST_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-white-list-by-program-id")
    public ResponseEntity<ApiResponse> getWhiteListByProgramId(@RequestParam Long programId) {
        ApiResponse response = null;
        try {
            List<IsdnList> list = servicePackageListService.getWhiteListByProgramId(programId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.GET_WHITE_LIST_FAIL);
            logger.error("GET_WHITE_LIST_FAIL", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-black-list-by-program-id")
    public ResponseEntity<ApiResponse> getBlackListByProgramId(@RequestParam Long programId) {
        ApiResponse response = null;
        try {
            List<IsdnList> list = servicePackageListService.getBlackListByProgramId(programId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), list);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.GET_BLACK_LIST_FAIL);
            logger.error("GET_BLACK_LIST_FAIL", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
