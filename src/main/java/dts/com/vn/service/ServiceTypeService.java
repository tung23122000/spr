package dts.com.vn.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.repository.ServiceTypeRepository;

@Service
public class ServiceTypeService {

  @Autowired
  private ServiceTypeRepository serviceTypeRepository;

  public List<ServiceType> findByStatusAndDisplayStatus() {
    return serviceTypeRepository.findByStatusAndDisplayStatus("1", "1");
  }
}
