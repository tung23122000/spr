package dts.com.vn.service;

import dts.com.vn.entities.*;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.Constant;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.*;
import dts.com.vn.request.AddServicePackageRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.util.DateTimeUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ServicePackageService {

	@Autowired
	private ServicePackageRepository servicePackageRepository;

	@Autowired
	private ServiceTypeRepository serviceTypeRepository;

	@Autowired
	private ServicesRepository servicesRepository;

	@Autowired
	private FlowGroupRepository flowGroupRepository;

	@Autowired
	private ServiceProgramRepository serviceProgramRepository;

	@Autowired
	private BucketsInfoRepository bucketsInfoRepository;

	@Autowired
	private MapServicePackageRepository mapServicePackageRepository;

	@Autowired
	private NdsTypeParamProgramRepository ndsTypeParamProgramRepository;

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
			servicePackage.setExtendStatus(request.getExtendStatus());
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

	public List<ServicePackage> findBlockPCRF(Long packageId) {
		ServicePackage servicePackage = findById(packageId);
		if (servicePackage.getPcrfGroup() == null) {
			return null;
		}
		return servicePackageRepository.findBlockPCRF(packageId, servicePackage.getPcrfGroup().getPcrfGroupId());
	}

	public List<ServicePackage> getAllWithoutPageable() {
		return servicePackageRepository.findAll();
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
			Long newPackageId = servicePackageRepository.save(service).getPackageId();
			// 2. Tìm những chương trình của gói cước cũ để clone
			List<ServiceProgram> listOldServiceProgram = serviceProgramRepository.findAllByPackageId(oldPackageId);
			// 3. Thực hiện clone với từng chương trình
			for (ServiceProgram oldProgram : listOldServiceProgram) {
				// 3.1 Clone chương trình
				ServiceProgram newProgram = cloneServiceProgram(oldProgram, newPackageId);
				// 3.1 Tìm những thông tin đấu nối IN của gói cước và chương trình cũ
				List<BucketsInfo> lstOldBucketInfos = bucketsInfoRepository.getListClone(oldPackageId, oldProgram.getProgramId());
				if (lstOldBucketInfos.size() > 0) {
					// 3.1.2 Thực hiện clone thông tin đấu nối IN nếu có
					for (BucketsInfo bi : lstOldBucketInfos) {
						cloneBucketInfo(bi, newPackageId, newProgram);
					}
				}
				// 3.2 Tìm những thông tin đấu nối BILLING của các
				List<MapServicePackage> lstOldMapServicePackages = mapServicePackageRepository.getListClone(oldPackageId, oldProgram.getProgramId());
				if (lstOldBucketInfos.size() > 0) {
					// 3.1.2 Thực hiện clone thông tin đấu nối IN nếu có
					for (MapServicePackage msp : lstOldMapServicePackages) {
						cloneMapServicePackage(msp, newPackageId, newProgram);
					}
				}
				// 3.3 Tìm những thông tin đấu nối PCRF
				List<NdsTypeParamProgram> lstOldNdsTypeParamPrograms = ndsTypeParamProgramRepository.getListClone(oldPackageId, oldProgram.getProgramId());
				// 3.1.2 Thực hiện clone thông tin đấu nối IN nếu có
				if (lstOldNdsTypeParamPrograms.size() > 0) {
					for (NdsTypeParamProgram ntpp : lstOldNdsTypeParamPrograms) {
						cloneNdsTypeParamProgram(ntpp, service, newProgram);
					}
				}
			}
			response.setStatus(ApiResponseStatus.SUCCESS.getValue());
			response.setMessage("Clone thông tin gói cước thành công");
			return response;
		}
	}

	/**
	 * Description - Hàm clone serviceProgram
	 *
	 * @param oldProgram danh sách những chương trình cần clone
	 * @param newId      ID của gói cước mới
	 * @author - giangdh
	 * @created - 8/27/2021
	 */
	@SneakyThrows(CloneNotSupportedException.class)
	private ServiceProgram cloneServiceProgram(ServiceProgram oldProgram, Long newId) {
		ServiceProgram serviceProgram = (ServiceProgram) oldProgram.clone();
		serviceProgram.setProgramId(null);
		serviceProgram.setServicePackage(servicePackageRepository.findByPackageId(newId));
		serviceProgram.setProgramCode(oldProgram.getProgramCode() + "_copy_" + System.currentTimeMillis());
		serviceProgramRepository.saveAndFlush(serviceProgram);
		return serviceProgram;
	}

	/**
	 * Description - Hàm clone BucketsInfo
	 *
	 * @param bi           is Obj
	 * @param newPackageId is Long
	 * @param newProgram   is Obj
	 * @author - giangdh
	 * @created - 8/27/2021
	 */
	@SneakyThrows(CloneNotSupportedException.class)
	private void cloneBucketInfo(BucketsInfo bi, Long newPackageId, ServiceProgram newProgram) {
		BucketsInfo bucketsInfo = (BucketsInfo) bi.clone();
		bucketsInfo.setBucketsId(null);
		bucketsInfo.setPackageId(newPackageId);
		bucketsInfo.setServiceProgram(newProgram);
		bucketsInfoRepository.saveAndFlush(bucketsInfo);
	}

	/**
	 * Description - Hàm clone MapServicePackage
	 *
	 * @param msp          is Obj
	 * @param newPackageId is Long
	 * @param newProgram   is Obj
	 * @author - giangdh
	 * @created - 8/27/2021
	 */
	@SneakyThrows(CloneNotSupportedException.class)
	private void cloneMapServicePackage(MapServicePackage msp, Long newPackageId, ServiceProgram newProgram) {
		MapServicePackage mapServicePackage = (MapServicePackage) msp.clone();
		mapServicePackage.setMapId(null);
		mapServicePackage.setPackageId(newPackageId);
		mapServicePackage.setServiceProgram(newProgram);
		mapServicePackageRepository.saveAndFlush(mapServicePackage);
	}

	/**
	 * Description - Hàm clone NdsTypeParamProgram
	 *
	 * @param ntpp       is Obj
	 * @param newPackage is Obj
	 * @param newProgram is Obj
	 * @author - giangdh
	 * @created - 8/27/2021
	 */
	@SneakyThrows(CloneNotSupportedException.class)
	private void cloneNdsTypeParamProgram(NdsTypeParamProgram ntpp, ServicePackage newPackage, ServiceProgram newProgram) {
		NdsTypeParamProgram ndsTypeParamProgram = (NdsTypeParamProgram) ntpp.clone();
		ndsTypeParamProgram.setNdsTypeParamKey(null);
		ndsTypeParamProgram.setServicePackage(newPackage);
		ndsTypeParamProgram.setServiceProgram(newProgram);
		ndsTypeParamProgramRepository.saveAndFlush(ndsTypeParamProgram);
	}

}
