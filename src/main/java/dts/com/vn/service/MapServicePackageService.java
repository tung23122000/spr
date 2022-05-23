package dts.com.vn.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

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
		Long maxId = mapServicePackageRepository.findIdMax();
		ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getProgramId())
				.orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
		ExternalSystem externalSystem = externalSystemRepository.findById(request.getExtSystemId())
				.orElseThrow(() -> new RestApiException(ErrorCode.EXTERNAL_SYSTEM_NOT_FOUND));
		MapServicePackage mapServicePackage = new MapServicePackage();
		mapServicePackage.setMapId(maxId+1L);
		mapServicePackage.setPackageId(serviceProgram.getServicePackage().getPackageId());
		mapServicePackage.setExtSystem(externalSystem);
		mapServicePackage.setServiceProgram(serviceProgram);
		mapServicePackage.setStaDate(DateTimeUtil.convertStringToInstant(request.getStartDate(), "dd/MM/yyyy HH:mm:ss"));
		if(request.getEndDate()!=null){
			mapServicePackage.setEndDate(DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss"));
		}else{
			mapServicePackage.setEndDate(null);
		}
		mapServicePackage.setPromCode(request.getPromCode());
		mapServicePackage.setMobType(request.getMobType());
		mapServicePackage.setPromDays(request.getPromDays());
		mapServicePackage.setOnOff(request.getOnOff());
		mapServicePackage.setRegMapCode(request.getRegMapCode());
		mapServicePackage.setDelMapCode(request.getDelMapCode());
		mapServicePackage.setChgMapCode(request.getChgMapCode());
		return mapServicePackageRepository.save(mapServicePackage);
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
		entity.setPromDays(request.getPromDays());
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

	public void delete(Long mapId){
		mapServicePackageRepository.deleteById(mapId);
	}
}
