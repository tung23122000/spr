package dts.com.vn.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dts.com.vn.entities.ExternalSystem;
import dts.com.vn.repository.ExternalSystemRepository;

@Service
public class ExternalSystemService {

  @Autowired
  private ExternalSystemRepository externalSystemRepository;
  
  public List<ExternalSystem> getData() {
    return externalSystemRepository.getData();
  }
}
