package dts.com.vn.service;

import dts.com.vn.entities.MapSmsRespond;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.MapSmsRespondRepository;
import dts.com.vn.repository.ServicePackageRepository;
import dts.com.vn.repository.ServiceProgramRepository;
import dts.com.vn.request.MapSmsRespondRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapSmsRespondService {
    private final MapSmsRespondRepository mapSmsRespondRepository;
    private final ServiceProgramRepository serviceProgramRepository;


    public MapSmsRespondService(MapSmsRespondRepository mapSmsRespondRepository, ServiceProgramRepository serviceProgramRepository) {
        this.mapSmsRespondRepository = mapSmsRespondRepository;
        this.serviceProgramRepository = serviceProgramRepository;
    }

    public MapSmsRespond save(MapSmsRespondRequest mapSmsRespondRequest) {
        ServiceProgram serviceProgram = serviceProgramRepository.findById(mapSmsRespondRequest.getProgramId()).orElse(null);
        MapSmsRespond mapSmsRespondExist = null;
        if (mapSmsRespondRequest.getMapSmsRespondId() == null) {
            // Case Create
            mapSmsRespondExist = mapSmsRespondRepository.findBySmsRespond(mapSmsRespondRequest.getSmsRespond());
        } else {
            // Case Edit
            mapSmsRespondExist = mapSmsRespondRepository.findBySmsRespond(mapSmsRespondRequest.getSmsRespond(), mapSmsRespondRequest.getMapSmsRespondId());
        }
        if (serviceProgram == null) {
            // package code ko tồn tại
            throw new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND);
        } else if (mapSmsRespondExist != null) {
            // Đã tồn tại 1 sms respond có nội dung giống request
            throw new RestApiException(ErrorCode.DUPLICATE_SMS_RESPOND);
        } else {
            MapSmsRespond mapSmsRespond = new MapSmsRespond(mapSmsRespondRequest);
            mapSmsRespond.setServiceProgram(serviceProgram);
            return mapSmsRespondRepository.save(mapSmsRespond);
        }
    }

    public List<MapSmsRespond> findAll(Long programId) {
        ServiceProgram serviceProgram = serviceProgramRepository.findById(programId).orElse(null);
        if (serviceProgram == null) {
            throw new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND);
        } else {
            return mapSmsRespondRepository.findAllByProgramId(programId);
        }
    }

    public void delete(Long mapSmsRespondId) {
        mapSmsRespondRepository.deleteById(mapSmsRespondId);
    }
}
