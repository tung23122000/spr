package dts.com.vn.controller;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import dts.com.vn.entities.MapServicePackage;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.AddMapServicePackageRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.MapServicePackageResponse;
import dts.com.vn.service.MapServicePackageService;

@RestController
@RequestMapping("/api/map-service-package")
public class MapServicePackageController {

	private static final Logger logger = LoggerFactory.getLogger(MapServicePackageController.class);

	@Autowired
	private MapServicePackageService mapServicePackageService;

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
			response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse> add(@RequestBody AddMapServicePackageRequest request) {
		ApiResponse response;
		try {
			logger.info("Add Billing request: {}", request.toString());
			MapServicePackage entity = mapServicePackageService.add(request);
			MapServicePackageResponse entityResponse = new MapServicePackageResponse(entity);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityResponse);
			logger.info("AddBilling response: {}", response.toString());
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
			logger.trace("Add Billing response: {}", response.toString());
		}
		return ResponseEntity.ok().body(response);
	}

	@PutMapping("/update")
	public ResponseEntity<ApiResponse> update(@RequestBody AddMapServicePackageRequest request) {
		ApiResponse response;
		try {
			MapServicePackage entity = mapServicePackageService.update(request);
			MapServicePackageResponse entityResponse = new MapServicePackageResponse(entity);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityResponse);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
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
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
		}
		return ResponseEntity.ok().body(response);
	}
}
