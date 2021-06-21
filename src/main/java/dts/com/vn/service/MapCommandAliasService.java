package dts.com.vn.service;

import dts.com.vn.entities.Condition;
import dts.com.vn.entities.MapCommandAlias;
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.MapCommandAliasRepository;
import dts.com.vn.repository.ServiceProgramRepository;
import dts.com.vn.request.MapCommandAliasRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MapCommandAliasService {

    private final MapCommandAliasRepository mapCommandAliasRepository;

    private final ServiceProgramRepository serviceProgramRepository;

    @Autowired
    public MapCommandAliasService(MapCommandAliasRepository mapCommandAliasRepository, ServiceProgramRepository serviceProgramRepository) {
        this.mapCommandAliasRepository = mapCommandAliasRepository;
        this.serviceProgramRepository = serviceProgramRepository;
    }

    public MapCommandAlias add(MapCommandAliasRequest request) {
        ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getServiceProgram())
                .orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
        return mapCommandAliasRepository.save(new MapCommandAlias(request, serviceProgram));
    }

    public MapCommandAlias update(MapCommandAliasRequest request) {
        MapCommandAlias entity = findById(request.getMapCommandAliasId());
        ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getServiceProgram())
                .orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
        entity.setIsActive(request.getIsActive());
        entity.setCommandAlias(request.getCommandAlias());
        entity.setTransCode(request.getTransCode());
        return mapCommandAliasRepository.save(new MapCommandAlias(request, serviceProgram));
    }

    public MapCommandAlias findById(Long mcaId) {
        return mapCommandAliasRepository.findById(mcaId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));
    }

    public Page<MapCommandAlias> findAll(Long programId, Pageable pageable) {
        return mapCommandAliasRepository.findAllByProgramId(programId, pageable);
    }
}
