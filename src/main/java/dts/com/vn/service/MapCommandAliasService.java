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
        // Tìm ra tất cả serviceProgram trùng smsMo
        List<ServiceProgram> list = mapCommandAliasRepository.findBySmsMo(request.getSmsMo());
        // Check trùng khoảng thời gian
        if (list.size() > 0) {
            for (ServiceProgram item: list) {
                // Check 2 service program có trùng lặp thời gian hay không?
                if (areTwoDateTimeRangesOverlapping(serviceProgram, item)) {
                    // Nếu 1 trường hợp trùng là loại bỏ luôn.
                    // Nếu tất cả trường hợp đều không trùng thì mới save
                    throw new RestApiException(ErrorCode.DUPLICATE_SMS_MO);
                }
            }
        }
        return mapCommandAliasRepository.save(new MapCommandAlias(request, serviceProgram));
    }

    public MapCommandAlias update(MapCommandAliasRequest request) {
        MapCommandAlias entity = findById(request.getCmdAliasId());
        ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getServiceProgram())
                .orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
        // Tìm ra tất cả serviceProgram trùng smsMo
        List<ServiceProgram> list = mapCommandAliasRepository.findBySmsMoAndCmdAliasId(request.getSmsMo(), request.getCmdAliasId());
        // Check trùng khoảng thời gian
        for (ServiceProgram item: list) {
//            Check 2 service program có trùng lặp thời gian hay không?
            if (areTwoDateTimeRangesOverlapping(serviceProgram, item)) {
                // Nếu 1 trường hợp trùng là loại bỏ luôn.
                // Nếu tất cả trường hợp đều không trùng thì mới save
                throw new RestApiException(ErrorCode.DUPLICATE_SMS_MO);
            }
        }
        entity.setCmdStatus(request.getCmdStatus());
        entity.setCmdAliasName(request.getCmdAliasName());
        entity.setSmsMo(request.getSmsMo());
        entity.setCmdTransCode(request.getCmdTransCode());
        return mapCommandAliasRepository.save(entity);
    }

    public MapCommandAlias findById(Long mcaId) {
        return mapCommandAliasRepository.findById(mcaId)
                .orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));
    }

    public Page<MapCommandAlias> findAll(Long programId, Pageable pageable) {
        return mapCommandAliasRepository.findAllByProgramId(programId, pageable);
    }

    public void delete(Long cmdAliasId){
        mapCommandAliasRepository.deleteById(cmdAliasId);
    }

    public boolean areTwoDateTimeRangesOverlapping(ServiceProgram serviceProgramA, ServiceProgram serviceProgramB) {
        if (serviceProgramA.getEndDate() == null) {
            if (serviceProgramB.getEndDate() == null) {
                return true;
            } else {
                if (serviceProgramB.getEndDate().toEpochMilli() >= serviceProgramA.getStaDate().toEpochMilli()) {
                    return true;
                }
            }
        } else {
            if (serviceProgramB.getEndDate() == null) {
                if (serviceProgramA.getEndDate().toEpochMilli() >= serviceProgramB.getStaDate().toEpochMilli()) {
                    return true;
                }
            } else {
                if (serviceProgramA.getEndDate().toEpochMilli() >= serviceProgramB.getStaDate().toEpochMilli() &&
                    serviceProgramA.getStaDate().toEpochMilli() <= serviceProgramB.getEndDate().toEpochMilli()) {
                    return true;
                }
            }
        }
        return false;
    }
}
