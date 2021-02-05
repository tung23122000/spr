package dts.com.vn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import dts.com.vn.entities.ExternalSystem;
import dts.com.vn.entities.MapServicePackage;
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.ExternalSystemRepository;
import dts.com.vn.repository.MapServicePackageRepository;
import dts.com.vn.repository.ServiceProgramRepository;
import dts.com.vn.request.AddMapServicePackageRequest;
import dts.com.vn.util.DateTimeUtil;

@Service
public class MapServicePackageService {

  @Autowired
  private MapServicePackageRepository mapServicePackageRepository;

  @Autowired
  private ExternalSystemRepository externalSystemRepository;

  @Autowired
  private ServiceProgramRepository serviceProgramRepository;

  public Page<MapServicePackage> findAll(String search, Pageable pageable) {
    if (StringUtils.hasLength(search)) {
      return mapServicePackageRepository.findAll(search, pageable);
    }
    return mapServicePackageRepository.findAll(pageable);
  }

  public MapServicePackage add(AddMapServicePackageRequest request) {
    ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getProgramId())
        .orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
    ExternalSystem externalSystem = externalSystemRepository.findById(request.getExtSystemId())
        .orElseThrow(() -> new RestApiException(ErrorCode.EXTERNAL_SYSTEM_NOT_FOUND));
    return mapServicePackageRepository
        .save(new MapServicePackage(request, externalSystem, serviceProgram));
  }

  public MapServicePackage update(AddMapServicePackageRequest request) {
    ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getProgramId())
        .orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
    ExternalSystem externalSystem = externalSystemRepository.findById(request.getExtSystemId())
        .orElseThrow(() -> new RestApiException(ErrorCode.EXTERNAL_SYSTEM_NOT_FOUND));
    MapServicePackage entity = findById(request.getMapId());
    entity.setExtSystem(externalSystem);
    entity.setPackageId(serviceProgram.getServicePackage().getPackageId());
    entity.setServiceProgram(serviceProgram);
    entity.setStaDate(
        DateTimeUtil.convertStringToInstant(request.getStartDate(), "dd/MM/yyyy HH:mm:ss"));
    entity.setEndDate(
        DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss"));
    entity.setPromCode(request.getPromCode());
    entity.setMobType(request.getMobType());
    entity.setPromDays(request.getPromDays().toString());
    entity.setOnOff(request.getOnOff());
    entity.setRegMapCode(request.getRegMapCode());
    entity.setDelMapCode(request.getDelMapCode());
    entity.setChgMapCode(request.getChgMapCode());
    return mapServicePackageRepository.save(entity);
  }

  public MapServicePackage findById(Long id) {
    return mapServicePackageRepository.findById(id)
        .orElseThrow(() -> new RestApiException(ErrorCode.MAP_SERVICE_PACKAGE_NOT_FOUND));
  }
}
