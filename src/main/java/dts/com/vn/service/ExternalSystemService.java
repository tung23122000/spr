package dts.com.vn.service;

import dts.com.vn.entities.ExternalSystem;
import dts.com.vn.repository.ExternalSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalSystemService {

	@Autowired
	private ExternalSystemRepository externalSystemRepository;

	public List<ExternalSystem> getData() {
		return externalSystemRepository.getData();
	}
}
