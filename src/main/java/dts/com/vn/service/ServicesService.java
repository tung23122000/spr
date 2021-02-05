package dts.com.vn.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dts.com.vn.entities.Services;
import dts.com.vn.repository.ServicesRepository;

@Service
public class ServicesService {

  @Autowired
  private ServicesRepository servicesRepository;
  
  public List<Services> getAll() {
    return servicesRepository.findAll();
  }
}
