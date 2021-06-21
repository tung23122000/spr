package dts.com.vn.controller;

import dts.com.vn.entities.MapCommandAlias;
import dts.com.vn.entities.NdsTypeParamProgram;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.MapCommandAliasRequest;
import dts.com.vn.request.NdsTypeParamProgramRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.MapCommandAliasResponse;
import dts.com.vn.response.NdsTypeParamProgramResponse;
import dts.com.vn.service.MapCommandAliasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/map-command-alias")
public class MapCommandAliasController {

    private final MapCommandAliasService mapCommandAliasService;

    @Autowired
    public MapCommandAliasController(MapCommandAliasService mapCommandAliasService) {
        this.mapCommandAliasService = mapCommandAliasService;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@RequestBody MapCommandAliasRequest request) {
        ApiResponse response;
        try {
            MapCommandAlias entity = mapCommandAliasService.add(request);
            MapCommandAliasResponse entityRes = new MapCommandAliasResponse(entity);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
        }
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> update(@RequestBody MapCommandAliasRequest request) {
        ApiResponse response;
        try {
            MapCommandAlias entity = mapCommandAliasService.update(request);
            MapCommandAliasResponse entityRes = new MapCommandAliasResponse(entity);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/get-all/{programId}")
    public ResponseEntity<ApiResponse> getAll(@PathVariable("programId") Long programId,
                                              @Qualifier(value = "pageableTransaction") Pageable pageableTransaction) {
        ApiResponse response;
        try {
            Page<MapCommandAlias> entity = mapCommandAliasService.findAll(programId, pageableTransaction);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entity);
        } catch (RestApiException ex) {
            response = new ApiResponse(ex);
        } catch (Exception e) {
            response = new ApiResponse(e, ErrorCode.API_FAILED_UNKNOWN);
        }
        return ResponseEntity.ok().body(response);
    }
}
