package dts.com.vn.service;

import com.google.gson.Gson;
import dts.com.vn.entities.ListCondition;
import dts.com.vn.entities.MapConditionProgram;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.ListConditionRepository;
import dts.com.vn.repository.MapConditionProgramRepository;
import dts.com.vn.request.MapConditionProgramRequest;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class MapConditionProgramService {

	private final MapConditionProgramRepository mapConditionProgramRepository;

	private final ListConditionRepository listConditionRepository;

	public MapConditionProgramService(MapConditionProgramRepository mapConditionProgramRepository, ListConditionRepository listConditionRepository) {
		this.mapConditionProgramRepository = mapConditionProgramRepository;
		this.listConditionRepository = listConditionRepository;
	}

	public MapConditionProgram save(MapConditionProgramRequest mapConditionProgramRequest) {
		ListCondition listCondition = listConditionRepository.findById(mapConditionProgramRequest.getConditionId()).orElse(null);
		if (listCondition == null) {
			throw new RestApiException(ErrorCode.LIST_CONDITION_ID_NOT_FOUND);
		}
		MapConditionProgram mapConditionProgramExist;
		if (mapConditionProgramRequest.getId() == null) {
			mapConditionProgramExist = mapConditionProgramRepository.findByProgramIdAndConditionId(
					mapConditionProgramRequest.getProgramId(), mapConditionProgramRequest.getConditionId());
		} else {
			mapConditionProgramExist = mapConditionProgramRepository.findByProgramIdAndConditionId(
					mapConditionProgramRequest.getProgramId(), mapConditionProgramRequest.getConditionId(), mapConditionProgramRequest.getId());
		}
		if (mapConditionProgramExist != null) {
			throw new RestApiException(ErrorCode.EXIST_MAP_CONDITION_PROGRAM);
		}
		Object rawConditionValue = mapConditionProgramRequest.getConditionValue();
		String jsonConditionValue = new Gson().toJson(rawConditionValue, LinkedHashMap.class);
		MapConditionProgram mapConditionProgram = new MapConditionProgram();
		mapConditionProgram.setId(mapConditionProgramRequest.getId());
		mapConditionProgram.setProgramId(mapConditionProgramRequest.getProgramId());
		mapConditionProgram.setConditionId(listCondition);
		mapConditionProgram.setConditionValue(jsonConditionValue);
		return mapConditionProgramRepository.save(mapConditionProgram);
	}

	public MapConditionProgram getOne(Long programId, Integer conditionId) {
		return mapConditionProgramRepository.findByProgramIdAndConditionId(programId, conditionId);
	}

}
