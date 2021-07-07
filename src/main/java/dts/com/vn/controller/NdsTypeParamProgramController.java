package dts.com.vn.controller;

import dts.com.vn.entities.NdsTypeParamProgram;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.NdsTypeParamProgramRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.NdsTypeParamProgramResponse;
import dts.com.vn.service.NdsTypeParamProgramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequestMapping("/api/nds-type-param-program")
public class NdsTypeParamProgramController {

	@Autowired
	private NdsTypeParamProgramService ndsTypeParamProgramService;

	private static final Logger logger = LoggerFactory.getLogger(NdsTypeParamProgramController.class);

	@GetMapping("/find-all-by-program-id/{programId}")
	public ResponseEntity<ApiResponse> findAllByProgramId(@PathVariable("programId") Long programId, Pageable pageable) {
		ApiResponse response;
		try {
			Page<NdsTypeParamProgram> page = ndsTypeParamProgramService.findAllByProgramId(programId, pageable);
			Page<NdsTypeParamProgramResponse> entityRes = page.map(NdsTypeParamProgramResponse::new);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.DATA_FAILED);
			logger.error("DATA_PCRF_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/find-all")
	public ResponseEntity<ApiResponse> findAll(@RequestParam(name = "search", required = false) String search, Pageable pageable) {
		ApiResponse response;
		try {
			Page<NdsTypeParamProgram> page = ndsTypeParamProgramService.findAll(search, pageable);
			Page<NdsTypeParamProgramResponse> entityRes =
					page.map(new Function<NdsTypeParamProgram, NdsTypeParamProgramResponse>() {

						@Override
						public NdsTypeParamProgramResponse apply(NdsTypeParamProgram e) {
							return new NdsTypeParamProgramResponse(e);
						}
					});
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.DATA_FAILED);
			logger.error("DATA_PCRF_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse> add(@RequestBody NdsTypeParamProgramRequest request) {
		ApiResponse response;
		try {
			NdsTypeParamProgram entity = ndsTypeParamProgramService.add(request);
			NdsTypeParamProgramResponse entityRes = new NdsTypeParamProgramResponse(entity);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
			logger.error("SERVICE_PROGRAM_NOT_FOUND", response);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.DATA_FAILED);
			logger.error("DATA_PCRF_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@PutMapping("/update")
	public ResponseEntity<ApiResponse> update(@RequestBody NdsTypeParamProgramRequest request) {
		ApiResponse response;
		try {
			NdsTypeParamProgram entity = ndsTypeParamProgramService.update(request);
			NdsTypeParamProgramResponse entityRes = new NdsTypeParamProgramResponse(entity);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
			logger.error("SERVICE_PROGRAM_NOT_FOUND", response);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.DATA_FAILED);
			logger.error("DATA_PCRF_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/find-by-id/{ndsId}")
	public ResponseEntity<ApiResponse> findById(@PathVariable("ndsId") Long ndsId) {
		ApiResponse response;
		try {
			NdsTypeParamProgram entity = ndsTypeParamProgramService.findById(ndsId);
			NdsTypeParamProgramResponse entityRes = new NdsTypeParamProgramResponse(entity);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
			logger.error("NDS_TYPE_PARAM_PROGRAM_NOT_FOUND", response);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.DATA_FAILED);
			logger.error("DATA_PCRF_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}
}
