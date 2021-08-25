package dts.com.vn.service;

import dts.com.vn.entities.SubServicePackage;
import dts.com.vn.repository.SubServicePackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SubServicePackageService {
    @Autowired
    private SubServicePackageRepository subServicePackageRepository;

    public List<Object[]> findById(Long packageId) {
        return subServicePackageRepository.findGroupByPackageId(packageId);
    }

    public List<Object[]> checkExist(SubServicePackage subServicePackage) {
        return subServicePackageRepository.checkExist(subServicePackage);
    }

    public void save(SubServicePackage subServicePackage) {
        subServicePackage.setIsActive("1");
        subServicePackageRepository.save(subServicePackage);
    }

    public void update(SubServicePackage subServicePackage) {
        subServicePackage.setIsActive("1");
        subServicePackageRepository.update(subServicePackage);
    }

    public void deActiveAll(List<Object[]> listCurrentSubServicePackage) {
        for (Object[] objects : listCurrentSubServicePackage) {
            SubServicePackage subServicePackage = new SubServicePackage();
            subServicePackage.setPackageId(((BigInteger) objects[0]).longValue());
            subServicePackage.setSubPackageId(((BigInteger) objects[1]).longValue());
            subServicePackage.setIsActive("0");
            subServicePackageRepository.update(subServicePackage);
        }
    }
}
