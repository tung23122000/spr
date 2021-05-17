package dts.com.vn.service;

import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.entities.Services;
import dts.com.vn.enumeration.Constant;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.ServicePackageRepository;
import dts.com.vn.repository.ServiceTypeRepository;
import dts.com.vn.repository.ServicesRepository;
import dts.com.vn.request.AddServicePackageRequest;
import dts.com.vn.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
public class ServicePackageService {

	@Autowired
	private ServicePackageRepository servicePackageRepository;

	@Autowired
	private ServiceTypeRepository serviceTypeRepository;

	@Autowired
	private ServicesRepository servicesRepository;

	public Page<ServicePackage> findAll(String search, Long serviceTypeId, Pageable pageable) {
		if (StringUtils.hasLength(search)) {
			if (Objects.nonNull(serviceTypeId)) {
				return servicePackageRepository.findAll(search, serviceTypeId, pageable);
			}
			return servicePackageRepository.findAll(search, pageable);
		} else {
			if (Objects.nonNull(serviceTypeId)) {
				return servicePackageRepository.findAll(serviceTypeId, pageable);
			}
		}
		return servicePackageRepository.findAll(pageable);
	}

	public Page<ServicePackage> findAllByGroupCodeAndServiceTypeId(String groupCode, Long serviceTypeId, Pageable pageable) {
		if (StringUtils.hasLength(groupCode) && Objects.nonNull(serviceTypeId)) {
			return servicePackageRepository.findAllByGroupCodeAndServiceTypeId(groupCode, serviceTypeId, pageable);
		} else {
			if (!StringUtils.hasLength(groupCode)) {
				return servicePackageRepository.findAllByServiceTypeId(serviceTypeId, pageable);
			} else {
				return servicePackageRepository.findAllByGroupCode(groupCode, pageable);
			}
		}
	}

	public ServicePackage add(AddServicePackageRequest request) {
		Services services = servicesRepository.findById(5L).get();
		ServiceType serviceType = serviceTypeRepository.findById(request.getServiceTypeId()).get();
		ServicePackage service = new ServicePackage(request, serviceType, services);
		return servicePackageRepository.save(service);
	}

	public ServicePackage findById(Long id) {
		return servicePackageRepository.findById(id).get();
	}

	public ServicePackage pending(Long id) {
		ServicePackage servicePackage = servicePackageRepository.findById(id).get();
		if (servicePackage.getStatus().equals(Constant.PENDING)){
			throw new RestApiException(ErrorCode.PACKAGE_PENDING);
		}else {
			servicePackage.setStatus(Constant.PENDING);
		}
		return servicePackageRepository.save(servicePackage);
	}

	public ServicePackage active(Long id) {
		ServicePackage servicePackage = servicePackageRepository.findById(id).get();
		servicePackage.setStatus(Constant.ACTIVE);
		return servicePackageRepository.save(servicePackage);
	}

	public ServicePackage findByCode(String code) {
		return servicePackageRepository.findByCode(code).orElse(null);
	}

	public ServicePackage findByCodeIgnoreId(String code, Long packageId) {
		return servicePackageRepository.findByCodeAndPackageIdIgnore(code, packageId).orElse(null);
	}

	public ServicePackage update(AddServicePackageRequest request) {
		ServicePackage servicePackage = findById(request.getServicePackageId());
		if (Objects.nonNull(servicePackage)) {
			ServiceType serviceType = serviceTypeRepository.findById(request.getServiceTypeId()).get();
			servicePackage.setServiceType(serviceType);
			Services service = servicesRepository.findById(5L).get();
			servicePackage.setServices(service);
			servicePackage.setMobType(request.getMobType());
			servicePackage.setCode(request.getPackageCode());
			servicePackage.setListId(request.getListAccount());
			servicePackage.setName(request.getPackageName());
			servicePackage.setGroupCode(request.getGroupCode());
			servicePackage.setGprsType(request.getCategoryData());
			servicePackage.setStaDate(
					DateTimeUtil.convertStringToInstant(request.getDateStart(), "dd/MM/yyyy HH:mm:ss"));
			servicePackage.setEndDate(
					DateTimeUtil.convertStringToInstant(request.getDateEnd(), "dd/MM/yyyy HH:mm:ss"));
			return servicePackageRepository.save(servicePackage);
		}
		throw new RestApiException(ErrorCode.API_FAILED_UNKNOWN);
	}
}
