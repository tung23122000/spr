package dts.com.vn.service;

import dts.com.vn.entities.ServiceInfo;
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.ServiceInfoRepository;
import dts.com.vn.repository.ServiceProgramRepository;
import dts.com.vn.request.AddServiceInfoRequest;
import dts.com.vn.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

@Service
public class ServiceInfoService {

	@Autowired
	private ServiceInfoRepository serviceInfoRepository;

	@Autowired
	private ServiceProgramRepository serviceProgramRepository;

	public Page<ServiceInfo> findAll(String search, Pageable pageable) {
		if (StringUtils.hasLength(search)) {
			return serviceInfoRepository.findAll(search, pageable);
		}
		return serviceInfoRepository.findAll(pageable);
	}

	public ServiceInfo add(AddServiceInfoRequest request) {
		ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getProgramId())
				.orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
		return serviceInfoRepository.save(new ServiceInfo(request, serviceProgram));
	}

	public ServiceInfo update(AddServiceInfoRequest request) {
		ServiceInfo serviceInfo = findById(request.getServiceInfoId());
		ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getProgramId())
				.orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
		serviceInfo.setServiceProgram(serviceProgram);
		serviceInfo.setPackageId(Objects.nonNull(serviceProgram.getServicePackage())
				? serviceProgram.getServicePackage().getPackageId()
				: null);
		serviceInfo.setInfoName(request.getInfoName());
		serviceInfo.setInfoValue(request.getInfoValue());
		serviceInfo.setDescription(request.getDescription());
		serviceInfo.setStaDate(
				DateTimeUtil.convertStringToInstant(request.getStaDate(), "dd/MM/yyyy HH:mm:ss"));
		serviceInfo.setEndDate(
				DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss"));
		return serviceInfoRepository.save(serviceInfo);
	}

	public ServiceInfo findById(Long id) {
		return serviceInfoRepository.findById(id)
				.orElseThrow(() -> new RestApiException(ErrorCode.NOT_FOUND));
	}

	public List<ServiceInfo> findAllByPackageId(Long packageId, Long programId) {
		return serviceInfoRepository.findAllByPackageId(packageId, programId);
	}

}
