package dts.com.vn.service;

import dts.com.vn.entities.ServiceType;
import dts.com.vn.repository.ServiceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceTypeService {

	@Autowired
	private ServiceTypeRepository serviceTypeRepository;

	public List<ServiceType> findByStatusAndDisplayStatus() {
		return serviceTypeRepository.findByStatusAndDisplayStatus("1", "1");
	}
}
