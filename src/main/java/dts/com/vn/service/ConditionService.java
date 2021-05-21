package dts.com.vn.service;

import dts.com.vn.entities.Condition;
import dts.com.vn.entities.MapConditionServicePackage;
import dts.com.vn.repository.ConditionRepository;
import dts.com.vn.repository.MapConditionServicePackageRepository;
import dts.com.vn.request.ConditionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConditionService {

    @Autowired
    private ConditionRepository conditionRepository;

    @Autowired
    private MapConditionServicePackageRepository mapConditionServicePackageRepository;

    public List<Condition> findAll() {
        return conditionRepository.findAll();
    }


    public MapConditionServicePackage saveCondition(ConditionRequest input, Long packageId, Long programId){
        MapConditionServicePackage mapConditionServicePackage = new MapConditionServicePackage();
        mapConditionServicePackage.setPackageId(packageId);
        mapConditionServicePackage.setProgramId(programId);
        mapConditionServicePackage.setConditionId(input.getConditionId());
        mapConditionServicePackage.setIsConfirm(input.getIsConfirm());
        mapConditionServicePackage.setMessageMt(input.getMessageMT());
        return mapConditionServicePackageRepository.save(mapConditionServicePackage);
    }
}
