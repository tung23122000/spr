package dts.com.vn.service;

import dts.com.vn.entities.SubServicePackage;
import dts.com.vn.repository.SubServicePackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubServicePackageService {
    @Autowired
    private SubServicePackageRepository subServicePackageRepository;

    public List<SubServicePackage> findById(Long packageId) {
        return subServicePackageRepository.findGroupByPackageId(packageId);
    }

    public SubServicePackage checkExist(SubServicePackage subServicePackage){
        return subServicePackageRepository.checkExist(subServicePackage.getPackageId(), subServicePackage.getSubPackageId());
    }

    public SubServicePackage save(SubServicePackage subServicePackage){
        return subServicePackageRepository.save(subServicePackage);
    }

    public void deleteAll(List<SubServicePackage> listCurrentSubServicePackage){
        subServicePackageRepository.deleteAll(listCurrentSubServicePackage);
    }
}
