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
import dts.com.vn.response.ServicePackage2Response;
import dts.com.vn.response.ServicePackageFoResponse;
import dts.com.vn.response.ServicePackageResponse;
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.util.DateTimeUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

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

    private final BucketsInfoRepository bucketsInfoRepository;

    private final SubPackageInfoRepository subPackageInfoRepository;

    public ServicePackageService(ServicePackageRepository servicePackageRepository,
                                 ServiceTypeRepository serviceTypeRepository,
                                 ServicesRepository servicesRepository,
                                 ServiceProgramRepository serviceProgramRepository,
                                 SubServicePackageRepository subServicePackageRepository,
                                 ServiceProgramService serviceProgramService,
                                 LogActionService logActionService, TokenProvider tokenProvider,
                                 BucketsInfoRepository bucketsInfoRepository,
                                 SubPackageInfoRepository subPackageInfoRepository) {
        this.servicePackageRepository = servicePackageRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.servicesRepository = servicesRepository;
        this.serviceProgramRepository = serviceProgramRepository;
        this.subServicePackageRepository = subServicePackageRepository;
        this.serviceProgramService = serviceProgramService;
        this.logActionService = logActionService;
        this.tokenProvider = tokenProvider;
        this.bucketsInfoRepository = bucketsInfoRepository;
        this.subPackageInfoRepository = subPackageInfoRepository;
    }

    public Page<ServicePackage> findAll(String search, Long serviceTypeId, Pageable pageable) {
        if (StringUtils.hasLength(search)) {
            try {
                Long packageId = Long.parseLong(search);
                if (Objects.nonNull(serviceTypeId)) {
                    return servicePackageRepository.findAll(packageId, serviceTypeId, pageable);
                }
                return servicePackageRepository.findAllByPackageId(packageId, pageable);
            } catch (NumberFormatException e) {
                if (Objects.nonNull(serviceTypeId)) {
                    return servicePackageRepository.findAll(search, serviceTypeId, pageable);
                }
                return servicePackageRepository.findAll(search, pageable);
            }
        } else {
            if (Objects.nonNull(serviceTypeId)) {
                return servicePackageRepository.findAll(serviceTypeId, pageable);
            }
        }
        return servicePackageRepository.findAll(pageable);
    }

    public Page<ServicePackage> findAllByGroupCodeAndServiceTypeId(String groupCode, Long serviceTypeId,
                                                                   Pageable pageable) {
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
        if (request.getFlexSubPackageId() != null) {
            List<ServiceProgram> listProgramFlexPackage = serviceProgramRepository.findAllActiveByPackageId(request.getFlexSubPackageId());
            if (listProgramFlexPackage.size() > 0) {
                for (ServiceProgram serviceProgram : listProgramFlexPackage) {
                    if (!serviceProgram.getIsDefaultProgram()) {
                        throw new RestApiException(ErrorCode.DEFAULT_PROGRAM_REQUIRED);
                    }
                }
            } else {
                throw new RestApiException(ErrorCode.SERVICE_PROGRAM_REQUIRED);
            }
        }
        ServicePackage newPackage = servicePackageRepository.save(service);
        if (request.getFlexSubPackageId() != null) {
            SubPackageInfo subPackageInfo = new SubPackageInfo();
            subPackageInfo.setSubPackageInfoId(null);
            subPackageInfo.setPackageId(newPackage.getPackageId());
            subPackageInfo.setPackageCode(newPackage.getCode());
            subPackageInfo.setFlexSubPackageId(request.getFlexSubPackageId());
            subPackageInfoRepository.save(subPackageInfo);
        }
        return newPackage;
    }

    public ServicePackage findById(Long id) {
        return servicePackageRepository.findById(id).get();
    }

    public ServicePackageResponse getDetaiById(Long id) {
        ServicePackage servicePackage = servicePackageRepository.findByPackageId(id);
        ServicePackageResponse response = new ServicePackageResponse();
        if (servicePackage != null) {
            response.setPackageId(servicePackage.getPackageId());
            response.setName(servicePackage.getName());
            response.setCode(servicePackage.getCode());
            response.setServiceTypeId(servicePackage.getServiceType().getServiceTypeId());
            response.setServiceTypeName(servicePackage.getServiceType().getName());
            response.setMobType(servicePackage.getMobType());
            response.setGroupCode(servicePackage.getGroupCode());
            response.setGprsType(servicePackage.getGprsType());
            response.setStaDate(Objects.nonNull(servicePackage.getStaDate())
                                ? DateTimeUtil.formatInstant(servicePackage.getStaDate(), "dd/MM/yyyy HH:mm:ss")
                                : "");
            response.setEndDate(Objects.nonNull(servicePackage.getEndDate())
                                ? DateTimeUtil.formatInstant(servicePackage.getEndDate(), "dd/MM/yyyy HH:mm:ss")
                                : "");
            response.setStatus(servicePackage.getStatus());
            response.setFlowGroupId(servicePackage.getFlowGroupId());
            response.setListId(servicePackage.getListId());
            response.setServiceId(servicePackage.getServices().getServiceId());
            response.setServiceName(servicePackage.getServices().getServiceName());
            response.setCountryCode(servicePackage.getCountryCode());
            response.setDelayTimeCVQT(servicePackage.getDelayTimeCVQT());
            response.setInputKey(servicePackage.getInputKey());
            response.setInputValue(servicePackage.getInputValue());
            response.setExcludePackageList(servicePackage.getExcludePackageList());
            response.setExpectResult(servicePackage.getExpectResult());
            response.setExtendStatus(servicePackage.getExtendStatus());
            response.setIsRetry(servicePackage.getIsRetry());
            response.setSystemOwner(servicePackage.getSystemOwner());
            SubPackageInfo fromDb = subPackageInfoRepository.findByPackageId(servicePackage.getPackageId());
            if (fromDb != null) {
                response.setFlexSubPackageId(fromDb.getFlexSubPackageId());
            }
        }
        return response;
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
            servicePackage.setDisplayStatus("1");
            SubPackageInfo fromDb = subPackageInfoRepository.findByPackageId(servicePackage.getPackageId());
            if (fromDb != null) {
                if (request.getFlexSubPackageId() != null) {
                    List<ServiceProgram> listProgramFlexPackage = serviceProgramRepository.findAllActiveByPackageId(request.getFlexSubPackageId());
                    if (listProgramFlexPackage.size() > 0) {
                        for (ServiceProgram serviceProgram : listProgramFlexPackage) {
                            if (!serviceProgram.getIsDefaultProgram()) {
                                throw new RestApiException(ErrorCode.DEFAULT_PROGRAM_REQUIRED);
                            }
                        }
                    } else {
                        throw new RestApiException(ErrorCode.SERVICE_PROGRAM_REQUIRED);
                    }
                    subPackageInfoRepository.upadetFlexSubPackageInfo(request.getFlexSubPackageId(), servicePackage.getPackageId());
                }
            } else {
                if (request.getFlexSubPackageId() != null) {
                    List<ServiceProgram> listProgramFlexPackage = serviceProgramRepository.findAllActiveByPackageId(request.getFlexSubPackageId());
                    if (listProgramFlexPackage.size() > 0) {
                        for (ServiceProgram serviceProgram : listProgramFlexPackage) {
                            if (!serviceProgram.getIsDefaultProgram()) {
                                throw new RestApiException(ErrorCode.DEFAULT_PROGRAM_REQUIRED);
                            }
                        }
                    } else {
                        throw new RestApiException(ErrorCode.SERVICE_PROGRAM_REQUIRED);
                    }
                    SubPackageInfo subPackageInfo = new SubPackageInfo();
                    subPackageInfo.setSubPackageInfoId(null);
                    subPackageInfo.setPackageId(servicePackage.getPackageId());
                    subPackageInfo.setPackageCode(servicePackage.getCode());
                    subPackageInfo.setFlexSubPackageId(request.getFlexSubPackageId());
                    subPackageInfoRepository.save(subPackageInfo);
                }
            }
            return servicePackageRepository.save(servicePackage);
        }
        throw new RestApiException(ErrorCode.API_FAILED_UNKNOWN);
    }

//	public List<BucketsInfo> findBucketsInfo(Long id) {
//		return servicePackageRepository.findBucketsInfo(id);
//	}
//
//	public List<ServicePackage> findBlockIN(Long packageId, BucketsInfo bucketsInfo) {
//		return servicePackageRepository.findBlockIN(packageId, bucketsInfo.getBucType(), bucketsInfo.getBucName());
//	}

    // Ch???n IN kh??ng c??ng nh??m
    public HashSet<ServicePackage> findBlockINWithoutServiceType(Long packageId) {
        ServicePackage servicePackage = servicePackageRepository.findByPackageId(packageId);
        if (servicePackage == null) {
            throw new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }
        List<BucketsInfo> bucketsInfoList = bucketsInfoRepository.findByPackageId(packageId);
        if (bucketsInfoList.size() == 0) {
            return null;
        } else {
            HashSet<ServicePackage> servicePackageList = new HashSet<>();
            for (BucketsInfo bucketInfo : bucketsInfoList) {
                List<ServicePackage> listBlockIN = servicePackageRepository.findBlockINWithoutServiceType(packageId, bucketInfo.getBucType(),
                                                                                                          bucketInfo.getBucName(), servicePackage.getServiceType()
                                                                                                                                                 .getServiceTypeId());
                servicePackageList.addAll(listBlockIN);
            }
            return servicePackageList;
        }
    }

    // Ch???n IN c??ng nh??m
    public HashSet<ServicePackage> findBlockINWithServiceType(Long packageId) {
        ServicePackage servicePackage = servicePackageRepository.findByPackageId(packageId);
        if (servicePackage == null) {
            throw new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }
        List<BucketsInfo> bucketsInfoList = bucketsInfoRepository.findByPackageId(packageId);
        if (bucketsInfoList.size() == 0) {
            return null;
        } else {
            HashSet<ServicePackage> servicePackageList = new HashSet<>();
            for (BucketsInfo bucketInfo : bucketsInfoList) {
                List<ServicePackage> listBlockIN = servicePackageRepository.findBlockINWithServiceType(packageId, bucketInfo.getBucType(),
                                                                                                       bucketInfo.getBucName(), servicePackage.getServiceType()
                                                                                                                                              .getServiceTypeId());
                servicePackageList.addAll(listBlockIN);
            }
            return servicePackageList;
        }
    }

    // Ch???n PCRF kh??ng c??ng nh??m
    public HashSet<ServicePackage> findBlockPCRFWithoutServiceType(Long packageId) {
        ServicePackage servicePackage = servicePackageRepository.findByPackageId(packageId);
        if (servicePackage == null) {
            throw new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }
        HashSet<ServicePackage> returnList = new HashSet<>();
        if (servicePackage.getPcrfGroup() == null) {
            return null;
        } else {
            String pcrfGroupId = servicePackage.getPcrfGroup();
            // T??ch chu???i th??nh m???ng ch???a id pcrf
            String[] arrPcrfGroupId = pcrfGroupId.split(",");
            if (arrPcrfGroupId.length > 0) {
                for (String item : arrPcrfGroupId) {
                    // V???i m???i id pcrf l???y ra list ch???n
                    List<ServicePackage> list = servicePackageRepository.findBlockPCRFWithoutServiceType(packageId,
                                                                                                         item, servicePackage.getServiceType()
                                                                                                                             .getServiceTypeId());
                    returnList.addAll(list);
                }
            }
        }
        return returnList;
    }

    // Ch???n PCRF c??ng nh??m
    public HashSet<ServicePackage> findBlockPCRFWithServiceType(Long packageId) {
        ServicePackage servicePackage = servicePackageRepository.findByPackageId(packageId);
        if (servicePackage == null) {
            throw new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }
        HashSet<ServicePackage> returnList = new HashSet<>();
        if (servicePackage.getPcrfGroup() == null) {
            return null;
        } else {
            String pcrfGroupId = servicePackage.getPcrfGroup();
            // T??ch chu???i th??nh m???ng ch???a id pcrf
            String[] arrPcrfGroupId = pcrfGroupId.split(",");
            if (arrPcrfGroupId.length > 0) {
                for (String item : arrPcrfGroupId) {
                    // V???i m???i id pcrf l???y ra list ch???n
                    List<ServicePackage> list = servicePackageRepository.findBlockPCRFWithServiceType(packageId,
                                                                                                      item, servicePackage.getServiceType()
                                                                                                                          .getServiceTypeId());
                    returnList.addAll(list);
                }
            }
        }
        return returnList;
    }

    public List<ServicePackage> getAllWithoutPageable() {
        return servicePackageRepository.findAll();
    }

    /**
     * Description - H??m clone sub_service_package khi clone g??i c?????c
     *
     * @param listBlock - Danh s??ch c??c g??i block
     * @param packageId - M?? g??i c?????c
     * @author - giangdh
     * @created - 07/03/2022
     */
    public void saveSubServicePackge(List<SubServicePackageRequest> listBlock, Long packageId) {
        if (listBlock.size() > 0) {
            for (SubServicePackageRequest block : listBlock) {
                SubServicePackage subServicePackage = new SubServicePackage();
                subServicePackage.setPackageId(packageId);
                subServicePackage.setSubPackageId(block.getPackageId());
                subServicePackageRepository.save(subServicePackage);
            }
        }
    }

    /**
     * Description - H??m clone t???t c??? th??ng tin g??i c?????c
     *
     * @param request as AddServicePackageRequest
     * @return any
     * @author - giangdh
     * @created - 8/26/2021
     */
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse cloneServicePackage(AddServicePackageRequest request) {
        Long oldPackageId = request.getOldServicePackageId();
        // 1. Ki???m tra xem c?? ID c???a g??i c?????c c?? kh??ng
        ApiResponse response = new ApiResponse();
        if (oldPackageId == null) {
            return new ApiResponse(ApiResponseStatus.FAILED.getValue(), null,
                                   ErrorCode.SERVICE_PACKAGE_ID_REQUIRED.getErrorCode(),
                                   ErrorCode.SERVICE_PACKAGE_ID_REQUIRED.getMessage());
        } else {
            // 2. L??u b???n ghi clone ????? l???y ID g??i c?????c m???i
            Services services = servicesRepository.findById(5L).orElse(null);
            ServiceType serviceType = serviceTypeRepository.findById(request.getServiceTypeId()).orElse(null);
            if (services == null || serviceType == null) {
                return new ApiResponse(ApiResponseStatus.FAILED.getValue(), null,
                                       ErrorCode.CLONE_REQUEST_DATA_FAIL.getErrorCode(),
                                       ErrorCode.CLONE_REQUEST_DATA_FAIL.getMessage());
            }
            ServicePackage service = new ServicePackage(request, serviceType, services);
            Long maxId = servicePackageRepository.findIdMax();
            service.setPackageId(maxId + 1);
            // 3. T???o record g??i c?????c m???i
            ServicePackage servicePackage = servicePackageRepository.save(service);
            // 4. L???y id c???a g??i clone
            Long newPackageId = servicePackage.getPackageId();
            // 5. T??m nh???ng ch????ng tr??nh c???a g??i c?????c c?? ????? clone
            List<ServiceProgram> listOldServiceProgram = serviceProgramRepository.findAllByPackageId(oldPackageId);
            // 6. Th???c hi???n clone v???i t???ng ch????ng tr??nh
            for (ServiceProgram oldProgram : listOldServiceProgram) {
                serviceProgramService.cloneOneServiceProgram(newPackageId, oldPackageId, service, oldProgram);
            }
            // 7. Th???c hi???n clone ch???n g??i c?????c
            saveSubServicePackge(request.getSubServicePackage(), servicePackage.getPackageId());
            // 8. Th???c hi???n clone g??i ph??? flex data n???u c??
            SubPackageInfo fromDb = subPackageInfoRepository.findByPackageId(oldPackageId);
            if (fromDb != null) {
                if (request.getFlexSubPackageId() != null) {
                    SubPackageInfo subPackageInfo = new SubPackageInfo();
                    subPackageInfo.setSubPackageInfoId(null);
                    subPackageInfo.setPackageId(newPackageId);
                    subPackageInfo.setPackageCode(servicePackage.getCode());
                    subPackageInfo.setFlexSubPackageId(request.getFlexSubPackageId());
                    subPackageInfoRepository.save(subPackageInfo);
                }
            }
            // 9. T???o log clone
            LogAction logAction = new LogAction();
            logAction.setTableAction("service_package");
            logAction.setAccount(TokenProvider.account);
            logAction.setAction("CREATE");
            logAction.setOldValue(null);
            logAction.setNewValue(servicePackage.toString());
            logAction.setTimeAction(new Date());
            logAction.setIdAction(servicePackage.getPackageId());
            logActionService.add(logAction);
            // 10. Tr??? v??? respone
            response.setStatus(ApiResponseStatus.SUCCESS.getValue());
            response.setData(servicePackage);
            response.setMessage("Clone th??ng tin g??i c?????c th??nh c??ng");
            return response;
        }
    }

    public void delete(ServicePackage servicePackage) {
        ApiResponse response = new ApiResponse();
        // T???o Log Action
        LogAction logAction = new LogAction();
        logAction.setTableAction("service_package");
        logAction.setAccount(TokenProvider.account);
        logAction.setAction("DELETE");
        logAction.setOldValue(servicePackage.toString());
        logAction.setNewValue(null);
        logAction.setTimeAction(new Date());
        logAction.setIdAction(servicePackage.getPackageId());
        logActionService.add(logAction);
        SubPackageInfo fromDb = subPackageInfoRepository.findByPackageId(servicePackage.getPackageId());
        if (fromDb != null) {
            subPackageInfoRepository.delete(fromDb);
        }
        List<ServiceProgram> listServiceProgram = serviceProgramService.findAllByPackageId(servicePackage.getPackageId());
        for (ServiceProgram serviceProgram : listServiceProgram) {
            serviceProgramService.delete(serviceProgram);
        }
        servicePackageRepository.delete(servicePackage);
    }

    public void switchServicePackage(Long packageId) {
        ServicePackage servicePackage = servicePackageRepository.findByPackageId(packageId);
        if (servicePackage == null) {
            throw new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        }
        servicePackage.setSystemOwner("FO");
        servicePackageRepository.save(servicePackage);
    }

    public ApiResponse findAllFOPackage() {
        ApiResponse response = new ApiResponse();
        List<ServicePackage> lstServicePackage = servicePackageRepository.findAllFOPackage();
        List<ServicePackageFoResponse> listPackageCode = lstServicePackage.stream()
                                                                          .map(developer -> new ServicePackageFoResponse(developer.getCode()))
                                                                          .collect(Collectors.toList());
        response.setStatus(ApiResponseStatus.SUCCESS.getValue());
        response.setData(listPackageCode);
        response.setMessage("L???y d??? li???u th??nh c??ng");
        return response;
    }

    public ApiResponse findAllPackage() {
        ApiResponse response = new ApiResponse();
        List<ServicePackage> packages = servicePackageRepository.findAll((Sort.by(Sort.Direction.ASC, "packageId")));
        List<ServicePackage2Response> listResponse = new ArrayList<>();
        if (packages.size() > 0) {
            for (ServicePackage aPackage : packages) {
                ServicePackage2Response servicePackage2Response = new ServicePackage2Response();
                servicePackage2Response.setPackageId(aPackage.getPackageId());
                servicePackage2Response.setPackageCode(aPackage.getCode());
                servicePackage2Response.setPackageName(aPackage.getName());
                listResponse.add(servicePackage2Response);
            }
        }
        response.setStatus(200);
        response.setData(listResponse);
        response.setMessage("L???y danh s??ch g??i c?????c th??nh c??ng!");
        return response;
    }


    public ApiResponse findAllFlexPackage() {
        ApiResponse response = new ApiResponse();
        List<ServicePackage> packages = servicePackageRepository.findAllFlexPackage();
        List<ServicePackage2Response> listResponse = new ArrayList<>();
        if (packages.size() > 0) {
            for (ServicePackage aPackage : packages) {
                ServicePackage2Response servicePackage2Response = new ServicePackage2Response();
                servicePackage2Response.setPackageId(aPackage.getPackageId());
                servicePackage2Response.setPackageCode(aPackage.getCode());
                servicePackage2Response.setPackageName(aPackage.getName());
                listResponse.add(servicePackage2Response);
            }
        }
        response.setStatus(200);
        response.setData(listResponse);
        response.setMessage("L???y danh s??ch g??i c?????c flex th??nh c??ng!");
        return response;
    }


}
