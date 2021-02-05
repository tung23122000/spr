package dts.com.vn.service;

import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.entities.Services;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.ServicePackageRepository;
import dts.com.vn.repository.ServiceTypeRepository;
import dts.com.vn.repository.ServicesRepository;
import dts.com.vn.request.AddServicePackageRequest;
import dts.com.vn.util.DateTimeUtil;

@Service
public class ServicePackageService {

  @Autowired
  private ServicePackageRepository servicePackageRepository;

  @Autowired
  private ServiceTypeRepository serviceTypeRepository;

  @Autowired
  private ServicesRepository servicesRepository;

  public Page<ServicePackage> findAll(String search, Pageable pageable) {
    if (StringUtils.hasLength(search))
      return servicePackageRepository.findAll(search, pageable);
    return servicePackageRepository.findAll(pageable);
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
