package dts.com.vn.controller;

import dts.com.vn.entities.SubServicePackage;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.SubServicePackageRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.SubServicePackageResponse;
import dts.com.vn.service.ServicePackageService;
import dts.com.vn.service.SubServicePackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/sub-service-package")
public class SubServicePackageController {
    @Autowired
    private SubServicePackageService subServicePackageService;

    @Autowired
    private ServicePackageService servicePackageService;

    private static final Logger logger = LoggerFactory.getLogger(SubServicePackageController.class);

    @GetMapping("/find-by-packageid/{packageId}")
    public ResponseEntity<ApiResponse> findByPackageId(@PathVariable(name = "packageId", required = true) Long packageId) {
        ApiResponse response;
        try {
            List<Object[]> listObject = subServicePackageService.findById(packageId);
            List<SubServicePackageResponse> returnList = new ArrayList<>();
            for (Object[] object : listObject) {
                Long firstResult = ((BigInteger) object[0]).longValue();
                Long secondResult = ((BigInteger) object[1]).longValue();
                SubServicePackageResponse subServicePackageResponse = new SubServicePackageResponse();
                if (firstResult.equals(packageId)) {
                    subServicePackageResponse.setPackageId(secondResult);
                    subServicePackageResponse.setName(servicePackageService.findById(secondResult).getName());
                }
                if (secondResult.equals(packageId)) {
                    subServicePackageResponse.setPackageId(firstResult);
                    subServicePackageResponse.setName(servicePackageService.findById(firstResult).getName());
                }
                returnList.add(subServicePackageResponse);
            }
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), returnList);
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
    @PostMapping("/save-sub-service-package/{packageId}")
    public ResponseEntity<ApiResponse> saveSubServicePackage(@PathVariable(name = "packageId", required = true) Long packageId,
                                                             @RequestBody List<SubServicePackageRequest> listSubServicePackage) {
        ApiResponse response;
        try {
            List<Object[]> listCurrentSubServicePackage = subServicePackageService.findById(packageId);
            subServicePackageService.deActiveAll(listCurrentSubServicePackage);
            for (SubServicePackageRequest request : listSubServicePackage) {
                SubServicePackage subServicePackage = new SubServicePackage();
                subServicePackage.setPackageId(packageId);
                subServicePackage.setSubPackageId(request.getPackageId());
                if (subServicePackageService.checkExist(subServicePackage).size() == 0) {
                    subServicePackageService.save(subServicePackage);
                } else {
                    subServicePackageService.update(subServicePackage);
                }
            }
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.DATA_FAILED);
            logger.error("SAVE_SUB_SERVICE_PACKAGE_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
