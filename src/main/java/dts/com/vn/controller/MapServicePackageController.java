package dts.com.vn.controller;

import dts.com.vn.entities.LogAction;
import dts.com.vn.entities.MapServicePackage;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.AddBucketsInfoRequest;
import dts.com.vn.request.AddMapServicePackageRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.MapServicePackageResponse;
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.service.LogActionService;
import dts.com.vn.service.MapServicePackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.function.Function;

@RestController
@RequestMapping("/api/map-service-package")
public class MapServicePackageController {

    private static final Logger logger = LoggerFactory.getLogger(MapServicePackageController.class);

    @Autowired
    private MapServicePackageService mapServicePackageService;

    @Autowired
    private LogActionService logActionService;

    @Autowired
    private TokenProvider tokenProvider;

    @GetMapping("/find-all")
    public ResponseEntity<ApiResponse> findAll(@RequestParam(name = "search", required = false) String search, Pageable pageable) {
        ApiResponse response;
        try {
            Page<MapServicePackage> page = mapServicePackageService.findAll(search, pageable);
            Page<MapServicePackageResponse> pageResponse =
                    page.map(new Function<MapServicePackage, MapServicePackageResponse>() {
                        @Override
                        public MapServicePackageResponse apply(MapServicePackage service) {
                            return new MapServicePackageResponse(service);
                        }
                    });
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), pageResponse);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.DATA_FAILED);
            logger.error("FIND_ALL_MAP_SERVICE_PACKAGE_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@RequestBody AddMapServicePackageRequest request) {
        ApiResponse response;
        try {
            MapServicePackage entity = mapServicePackageService.add(request);
            //			Tạo Log Action
            LogAction logAction = new LogAction();
            logAction.setTableAction("map_service_package");
            logAction.setAccount(tokenProvider.account);
            logAction.setAction("CREATE");
            logAction.setOldValue(null);
            logAction.setIdAction(entity.getMapId());
            logAction.setNewValue(entity.toString());
            logAction.setTimeAction(new Date());
            logActionService.add(logAction);

            MapServicePackageResponse entityResponse = new MapServicePackageResponse(entity);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityResponse);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
            logger.error("ADD_MAP_SERVICE_PACKAGE_FAILED", response);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.DATA_FAILED);
            ;
            logger.trace("Add Billing response: {}", response.toString());
            logger.error("ADD_MAP_SERVICE_PACKAGE_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> update(@RequestBody AddMapServicePackageRequest request) {
        ApiResponse response;
        try {
            // Tạo Log Action
            LogAction logAction = new LogAction();
            logAction.setTableAction("map_service_package");
            logAction.setAccount(tokenProvider.account);
            logAction.setAction("UPDATE");
            logAction.setOldValue(mapServicePackageService.findById(request.getMapId()).toString());

            MapServicePackage entity = mapServicePackageService.update(request);

            // Sau khi update set New Value
            logAction.setIdAction(entity.getMapId());
            logAction.setNewValue(entity.toString());
            logAction.setTimeAction(new Date());
            logActionService.add(logAction);

            MapServicePackageResponse entityResponse = new MapServicePackageResponse(entity);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityResponse);
        } catch (RestApiException ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex);
            logger.error("UPDATE_MAP_SERVICE_PACKAGE_FAILED", response);
        } catch (Exception e) {
            e.printStackTrace();
            response = new ApiResponse(e, ErrorCode.DATA_FAILED);
            ;
            logger.error("UPDATE_MAP_SERVICE_PACKAGE_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<ApiResponse> findById(@PathVariable("id") Long id) {
        ApiResponse response;
        try {
            MapServicePackage entity = mapServicePackageService.findById(id);
            MapServicePackageResponse entityResponse = new MapServicePackageResponse(entity);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityResponse);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
            logger.error("FIND_BY_ID_MAP_SERVICE_PACKAGE_FAILED", response);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.DATA_FAILED);
            ;
            logger.error("UPDATE_MAP_SERVICE_PACKAGE_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }

    @Transactional
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> delete(@RequestBody AddMapServicePackageRequest addMapServicePackageRequest) {
        ApiResponse response = null;
        try {
            MapServicePackage mapServicePackage = mapServicePackageService.findById(addMapServicePackageRequest.getMapId());
            // Tạo Log Action
            LogAction logAction = new LogAction();
            logAction.setTableAction("map_service_package");
            logAction.setAccount(tokenProvider.account);
            logAction.setAction("DELETE");
            logAction.setOldValue(mapServicePackage.toString());
            logAction.setNewValue(null);
            logAction.setTimeAction(new Date());
            logAction.setIdAction(mapServicePackage.getMapId());
            logActionService.add(logAction);
            mapServicePackageService.delete(mapServicePackage.getMapId());
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex, ErrorCode.DELETE_MAP_SERVICE_PACKAGE_FAILED);
            logger.error("DELETE_MAP_SERVICE_PACKAGE_FAILED", response);
        }
        return ResponseEntity.ok().body(response);
    }
}
