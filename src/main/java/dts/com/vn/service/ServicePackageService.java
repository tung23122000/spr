package dts.com.vn.service;

import dts.com.vn.entities.*;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.Constant;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.*;
import dts.com.vn.request.AddServicePackageRequest;
import dts.com.vn.request.SubServicePackageRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.util.DateTimeUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class ServicePackageService {
	private final ServicePackageRepository servicePackageRepository;
	private final ServiceTypeRepository serviceTypeRepository;
	private final ServicesRepository servicesRepository;
	private final ServiceProgramRepository serviceProgramRepository;
	private final SubServicePackageRepository subServicePackageRepository;
	private final ServiceProgramService serviceProgramService;
	private final LogActionService logActionService;
	private final TokenProvider tokenProvider;

	public ServicePackageService(ServicePackageRepository servicePackageRepository, ServiceTypeRepository serviceTypeRepository,
								 ServicesRepository servicesRepository, ServiceProgramRepository serviceProgramRepository,
								 SubServicePackageRepository subServicePackageRepository, ServiceProgramService serviceProgramService,
								 LogActionService logActionService, TokenProvider tokenProvider) {
		this.servicePackageRepository = servicePackageRepository;
		this.serviceTypeRepository = serviceTypeRepository;
		this.servicesRepository = servicesRepository;
		this.serviceProgramRepository = serviceProgramRepository;
		this.subServicePackageRepository = subServicePackageRepository;
		this.serviceProgramService = serviceProgramService;
		this.logActionService = logActionService;
		this.tokenProvider = tokenProvider;
	}

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
		Date now = new Date();
		long diff = (now.getTime() - servicePackage.getUpdateDate().getTime()) / 1000;
		if (servicePackage.getStatus().equals(Constant.PENDING) && diff <= Constant.TIME_TO_LIVE_PAGE) {
			throw new RestApiException(ErrorCode.PACKAGE_PENDING);
		} else {
			servicePackage.setStatus(Constant.PENDING);
			servicePackage.setUpdateDate(now);
		}
		return servicePackageRepository.save(servicePackage);
	}

	public ServicePackage active(Long id) {
		ServicePackage servicePackage = servicePackageRepository.findById(id).get();
		servicePackage.setStatus(Constant.ACTIVE);
		servicePackage.setUpdateDate(new Date());
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
			servicePackage.setUpdateDate(new Date());
			servicePackage.setCountryCode(request.getCountryCode());
			servicePackage.setDelayTimeCVQT(request.getDelayTimeCVQT());
			servicePackage.setFlowGroupId(request.getFlowGroupId());
			servicePackage.setPcrfGroup(request.getPcrfGroup());
			servicePackage.setInputKey(request.getInputKey());
			servicePackage.setInputValue(request.getInputValue());
			servicePackage.setExcludePackageList(request.getExcludePackageList());
			servicePackage.setExpectResult(request.getExpectResult());
			servicePackage.setExtendStatus(request.getExtendStatus());
			servicePackage.setIsRetry(request.getIsRetry());
			servicePackage.setSystemOwner(request.getSystemOwner());
			return servicePackageRepository.save(servicePackage);
		}
		throw new RestApiException(ErrorCode.API_FAILED_UNKNOWN);
	}

	public List<BucketsInfo> findBucketsInfo(Long id) {
		return servicePackageRepository.findBucketsInfo(id);
	}

	public List<ServicePackage> findBlockIN(Long packageId, BucketsInfo bucketsInfo) {
		return servicePackageRepository.findBlockIN(packageId, bucketsInfo.getBucType(), bucketsInfo.getBucName());
	}

	public HashSet<ServicePackage> findBlockPCRF(Long packageId) {
		ServicePackage servicePackage = findById(packageId);
		HashSet<ServicePackage> returnList = new HashSet<>();
		if (servicePackage.getPcrfGroup() == null) {
			return null;
		} else {
			String pcrfGroupId = servicePackage.getPcrfGroup();
			// Tách chuỗi thành mảng chứa id pcrf
			String[] arrPcrfGroupId = pcrfGroupId.split(",");
			if (arrPcrfGroupId.length > 0) {
				for (String item: arrPcrfGroupId) {
					// Với mỗi id pcrf lấy ra list chặn
					List<ServicePackage> list = servicePackageRepository.findBlockPCRF(packageId, item);
					returnList.addAll(list);
				}
			}
		}
		return returnList;
	}

	public List<ServicePackage> getAllWithoutPageable() {
		return servicePackageRepository.findAll();
	}

	public void saveSubServicePackge(List<SubServicePackageRequest> listBlock, Long packageId){
		if (listBlock.size()>0){
			for (SubServicePackageRequest block: listBlock) {
				SubServicePackage subServicePackage = new SubServicePackage();
				subServicePackage.setPackageId(packageId);
				subServicePackage.setSubPackageId(block.getPackageId());
				subServicePackageRepository.save(subServicePackage);
			}
		}
	}

	/**
	 * Description - Hàm clone tất cả thông tin gói cước
	 *
	 * @param request as AddServicePackageRequest
	 * @return any
	 * @author - giangdh
	 * @created - 8/26/2021
	 */
	@Transactional(rollbackFor = Exception.class)
	public ApiResponse cloneServicePackage(AddServicePackageRequest request) {
		Long oldPackageId = request.getOldServicePackageId();
		// 1. Kiểm tra xem có ID của gói cước cũ không
		ApiResponse response = new ApiResponse();
		if (oldPackageId == null) {
			return new ApiResponse(ApiResponseStatus.FAILED.getValue(), null,
					ErrorCode.SERVICE_PACKAGE_ID_REQUIRED.getErrorCode(),
					ErrorCode.SERVICE_PACKAGE_ID_REQUIRED.getMessage());
		} else {
			// 1. Lưu bản ghi clone để lấy ID gói cước mới
			Services services = servicesRepository.findById(5L).orElse(null);
			ServiceType serviceType = serviceTypeRepository.findById(request.getServiceTypeId()).orElse(null);
			if (services == null || serviceType == null) {
				return new ApiResponse(ApiResponseStatus.FAILED.getValue(), null,
						ErrorCode.CLONE_REQUEST_DATA_FAIL.getErrorCode(),
						ErrorCode.CLONE_REQUEST_DATA_FAIL.getMessage());
			}
			ServicePackage service = new ServicePackage(request, serviceType, services);
			ServicePackage servicePackage = servicePackageRepository.save(service);

			// Tạo Log Action CREATE ServicePackage
			LogAction logAction = new LogAction();
			logAction.setTableAction("service_package");
			logAction.setAccount(tokenProvider.account);
			logAction.setAction("CREATE");
			logAction.setOldValue(null);
			logAction.setNewValue(servicePackage.toString());
			logAction.setTimeAction(new Date());
			logAction.setIdAction(servicePackage.getPackageId());
			logActionService.add(logAction);
			//
			Long newPackageId = servicePackage.getPackageId();

			// 2. Tìm những chương trình của gói cước cũ để clone
			List<ServiceProgram> listOldServiceProgram = serviceProgramRepository.findAllByPackageId(oldPackageId);
			// 3. Thực hiện clone với từng chương trình
			for (ServiceProgram oldProgram : listOldServiceProgram) {
				serviceProgramService.cloneOneServiceProgram(newPackageId, oldPackageId, service, oldProgram);
			}
			// 4. Thực hiện chặn gói cước
			saveSubServicePackge(request.getSubServicePackage(), servicePackage.getPackageId());
			response.setStatus(ApiResponseStatus.SUCCESS.getValue());
			response.setData(servicePackage);
			response.setMessage("Clone thông tin gói cước thành công");
			return response;
		}
	}

	public void delete(ServicePackage servicePackage) {
		ApiResponse response = new ApiResponse();
		// Tạo Log Action
		LogAction logAction = new LogAction();
		logAction.setTableAction("service_package");
		logAction.setAccount(tokenProvider.account);
		logAction.setAction("DELETE");
		logAction.setOldValue(servicePackage.toString());
		logAction.setNewValue(null);
		logAction.setTimeAction(new Date());
		logAction.setIdAction(servicePackage.getPackageId());
		logActionService.add(logAction);
		List<ServiceProgram> listServiceProgram = serviceProgramService.findAllByPackageId(servicePackage.getPackageId());
		for (ServiceProgram serviceProgram: listServiceProgram) {
			serviceProgramService.delete(serviceProgram);
		}
		servicePackageRepository.delete(servicePackage);
	}


}
