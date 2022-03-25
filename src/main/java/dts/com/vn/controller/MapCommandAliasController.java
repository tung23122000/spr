package dts.com.vn.controller;

import dts.com.vn.entities.LogAction;
import dts.com.vn.entities.MapCommandAlias;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.MapCommandAliasRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.MapCommandAliasResponse;
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.service.LogActionService;
import dts.com.vn.service.MapCommandAliasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/map-command-alias")
public class MapCommandAliasController {

	private static final Logger logger = LoggerFactory.getLogger(MapCommandAliasController.class);
	private final MapCommandAliasService mapCommandAliasService;
	private final TokenProvider tokenProvider;
	private final LogActionService logActionService;

	@Autowired
	public MapCommandAliasController(MapCommandAliasService mapCommandAliasService, TokenProvider tokenProvider, LogActionService logActionService) {
		this.mapCommandAliasService = mapCommandAliasService;
		this.tokenProvider = tokenProvider;
		this.logActionService = logActionService;
	}

	@Transactional
	@PostMapping("/add")
	public ResponseEntity<ApiResponse> add(@RequestBody MapCommandAliasRequest request) {
		ApiResponse response;
		try {
			MapCommandAlias entity = mapCommandAliasService.add(request);
			LogAction logAction = new LogAction();
			logAction.setTableAction("map_command_alias");
			logAction.setAccount(tokenProvider.account);
			logAction.setAction("CREATE");
			logAction.setOldValue(null);
			logAction.setIdAction(entity.getCmdAliasId());
			logAction.setNewValue(entity.toString());
			logAction.setTimeAction(new Date());
			logActionService.add(logAction);
			MapCommandAliasResponse entityRes = new MapCommandAliasResponse(entity);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), entityRes);
		} catch (RestApiException ex) {
			response = new ApiResponse(ex);
		} catch (Exception e) {
			response = new ApiResponse(e, ErrorCode.ADD_COMMAND_ALIAS_FAILED);
			logger.error("ADD_COMMAND_ALIAS_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}

	@Transactional
	@PostMapping("/update")
	public ResponseEntity<ApiResponse> update(@RequestBody MapCommandAliasRequest request) {
		try {
			// Tạo Log Action
			LogAction logAction = new LogAction();
			logAction.setTableAction("map_command_alias");
			logAction.setAccount(TokenProvider.account);
			logAction.setAction("UPDATE");
			logAction.setOldValue(mapCommandAliasService.findById(request.getCmdAliasId()).toString());
			// Thực hiện update
			MapCommandAlias entity = mapCommandAliasService.update(request);
			logAction.setIdAction(entity.getCmdAliasId());
			logAction.setNewValue(entity.toString());
			logAction.setTimeAction(new Date());
			logActionService.add(logAction);
			// Trả về response
			MapCommandAliasResponse mcaResponse = new MapCommandAliasResponse(entity);
			ApiResponse response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), mcaResponse);
			logger.info("Response: " + response);
			return ResponseEntity.ok().body(response);
		} catch (Exception e) {
			ApiResponse response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null,"00",e.getMessage());
			logger.error("Response error: " + response);
			return ResponseEntity.ok().body(response);
		}
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

	@Transactional
	@PostMapping("/delete")
	public ResponseEntity<ApiResponse> delete(@RequestBody MapCommandAlias mapCommandAlias) {
		ApiResponse response = null;
		try {
			mapCommandAliasService.delete(mapCommandAlias.getCmdAliasId());
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null);
			LogAction logAction = new LogAction();
			logAction.setTableAction("map_command_alias");
			logAction.setAccount(tokenProvider.account);
			logAction.setAction("DELETE");
			logAction.setOldValue(mapCommandAlias.toString());
			logAction.setNewValue(null);
			logAction.setTimeAction(new Date());
			logAction.setIdAction(mapCommandAlias.getCmdAliasId());
			logActionService.add(logAction);
		} catch (Exception ex) {
			ex.printStackTrace();
			response = new ApiResponse(ex, ErrorCode.DELETE_MAP_COMMAND_ALIAS_FAILED);
			logger.error("DELETE_MAP_COMMAND_ALIAS_FAILED", response);
		}
		return ResponseEntity.ok().body(response);
	}
}
