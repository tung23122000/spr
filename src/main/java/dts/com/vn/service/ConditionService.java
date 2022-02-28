package dts.com.vn.service;

import dts.com.vn.entities.Condition;
import dts.com.vn.entities.MapConditionServicePackage;
import dts.com.vn.repository.ConditionRepository;
import dts.com.vn.repository.MapConditionServicePackageRepository;
import dts.com.vn.request.ConditionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConditionService {

	private final ConditionRepository conditionRepository;

	private final MapConditionServicePackageRepository mapConditionServicePackageRepository;

	@Autowired
	public ConditionService(ConditionRepository conditionRepository, MapConditionServicePackageRepository mapConditionServicePackageRepository) {
		this.conditionRepository = conditionRepository;
		this.mapConditionServicePackageRepository = mapConditionServicePackageRepository;
	}

	public List<Condition> findAll() {
		return conditionRepository.findAll(Sort.by(Sort.Direction.ASC, "conditionId"));
	}

	public MapConditionServicePackage saveCondition(ConditionRequest input, Long packageId, Long programId) {
		MapConditionServicePackage mapConditionServicePackage = new MapConditionServicePackage();
		mapConditionServicePackage.setPackageId(packageId);
		mapConditionServicePackage.setProgramId(programId);
		mapConditionServicePackage.setCondition(conditionRepository.getOne(input.getConditionId()));
		mapConditionServicePackage.setIsConfirm(input.getIsConfirm());
		mapConditionServicePackage.setMessageMt(input.getMessageMt());
		mapConditionServicePackage.setIsSoapConfirm(input.getIsSoapConfirm());
		mapConditionServicePackage.setIsChange(input.getIsChange());
		return mapConditionServicePackageRepository.save(mapConditionServicePackage);
	}

	public List<MapConditionServicePackage> getCondition(Long packageId, Long programId) {
		return mapConditionServicePackageRepository.getCondition(packageId, programId);
	}

	public void deleteAllMap(Long packageId, Long programId) {
		List<MapConditionServicePackage> list = mapConditionServicePackageRepository.getCondition(packageId, programId);
		for (MapConditionServicePackage item : list) {
			mapConditionServicePackageRepository.delete(item);
		}
	}
}
