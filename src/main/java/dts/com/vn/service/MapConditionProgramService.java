package dts.com.vn.service;

import dts.com.vn.entities.ListCondition;
import dts.com.vn.entities.MapConditionProgram;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.ListConditionRepository;
import dts.com.vn.repository.MapConditionProgramRepository;
import dts.com.vn.request.MapConditionProgramRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

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
        MapConditionProgram mapConditionProgram = new MapConditionProgram();
        mapConditionProgram.setProgramId(mapConditionProgramRequest.getProgramId());
        mapConditionProgram.setConditionId(listCondition);
        mapConditionProgram.setConditionValue(mapConditionProgramRequest.getConditionValue());
        return mapConditionProgramRepository.save(mapConditionProgram);
    }

    public MapConditionProgram getOne(Long programId, Integer conditionId) {
        return mapConditionProgramRepository.findByProgramIdAndConditionId(programId, conditionId);
    }
}
