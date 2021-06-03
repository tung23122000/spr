package dts.com.vn.service;

import dts.com.vn.entities.ServicePackageList;
import dts.com.vn.repository.ServicePackageListRepository;
import org.springframework.stereotype.Service;

@Service
public class ServicePackageListService {
    private final ServicePackageListRepository servicePackageListRepository;

    public ServicePackageListService(ServicePackageListRepository servicePackageListRepository) {
        this.servicePackageListRepository = servicePackageListRepository;
    }

    public ServicePackageList save(ServicePackageList servicePackageList){
        return servicePackageListRepository.save(servicePackageList);
    }
}
