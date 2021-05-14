package dts.com.vn.service;

import dts.com.vn.entities.Services;
import dts.com.vn.repository.ServicesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServicesService {

	@Autowired
	private ServicesRepository servicesRepository;

	public List<Services> getAll() {
		return servicesRepository.findAll();
	}
}
