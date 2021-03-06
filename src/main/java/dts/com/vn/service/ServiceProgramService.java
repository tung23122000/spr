package dts.com.vn.service;

import dts.com.vn.entities.*;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.*;
import dts.com.vn.request.AddServiceProgramRequest;
import dts.com.vn.response.*;
import dts.com.vn.response.service_package_detail.DetailServicePackageResponse;
import dts.com.vn.response.service_package_detail.DetailServiceProgramResponse;
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
import java.util.function.Function;
import java.util.regex.Pattern;

@Service
public class ServiceProgramService {

    private final SubServiceProgramRepository subServiceProgramRepository;

    @Autowired
    private ServiceProgramRepository serviceProgramRepository;

    @Autowired
    private ServicePackageRepository servicePackageRepository;

    @Autowired
    private BucketsInfoRepository bucketsInfoRepository;

    @Autowired
    private MapServicePackageRepository mapServicePackageRepository;

    @Autowired
    private NdsTypeParamProgramRepository ndsTypeParamProgramRepository;

    @Autowired
    private ServiceInfoRepository serviceInfoRepository;

    @Autowired
    private MinusMoneyRepository minusMoneyRepository;

    @Autowired
    private ActioncodeMappingRepository actioncodeMappingRepository;

    @Autowired
    private MapCommandAliasRepository mapCommandAliasRepository;

    @Autowired
    private MapSmsRespondRepository mapSmsRespondRepository;

    @Autowired
    private CcspInfoRepository ccspInfoRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private LogActionService logActionService;

    public ServiceProgramService(
            SubServiceProgramRepository subServiceProgramRepository) {this.subServiceProgramRepository = subServiceProgramRepository;}

    public Page<ServiceProgram> findAll(String search, Pageable pageable) {
        if (StringUtils.hasLength(search))
            return serviceProgramRepository.findAll(search, pageable);
        return serviceProgramRepository.findAll(pageable);
    }

    public ServiceProgram add(AddServiceProgramRequest request) {
        ServicePackage servicePackage =
                servicePackageRepository.findByCode(request.getPackageCode()).get();
        ServiceProgram serviceProgram = new ServiceProgram(request, servicePackage);
        // T??m ra t???t c??? serviceProgram tr??ng program_code
        List<ServiceProgram> listFindByProgramCode = serviceProgramRepository.findByProgramCode(request.getProgramCode());
        if (listFindByProgramCode.size() > 0) {
            for (ServiceProgram item : listFindByProgramCode) {
                // Check 2 service program c?? tr??ng l???p th???i gian hay kh??ng?
                if (areTwoDateTimeRangesOverlapping(serviceProgram, item)) {
                    // N???u 1 tr?????ng h???p tr??ng l?? lo???i b??? lu??n.
                    // N???u t???t c??? tr?????ng h???p ?????u kh??ng tr??ng th?? m???i save
                    throw new RestApiException(ErrorCode.DUPLICATE_PROGRAM_CODE);
                }
            }
        }

        if (request.getProgramCode().trim().equals("")) {
            request.setProgramCode(null);
        }
        // Check DEFAULT_PROGRAM
        if (request.getIsDefaultProgram()) {
            List<ServiceProgram> listFindDefaultProgram = serviceProgramRepository.findDefaultProgram(servicePackage.getPackageId());
            if (listFindDefaultProgram.size() > 0) {
                for (ServiceProgram item : listFindDefaultProgram) {
                    item.setIsDefaultProgram(false);
                    serviceProgramRepository.save(item);
                }
            }
        }
        String pattern1 = "([A-Za-z0-9]+(,*[A-Za-z0-9]+)*)";
        String pattern2 = "([A-Za-z0-9]+(\\s*[A-Za-z0-9]+)*)";
        String pattern3 = "([A-Za-z0-9]+(#*[A-Za-z0-9]+)*)";
        if (request.getCcspServiceCode() != null) {
            if (!Pattern.matches(pattern1, request.getCcspServiceCode()) && !Pattern.matches(pattern2, request.getCcspServiceCode())
                    && !Pattern.matches(pattern3, request.getCcspServiceCode())) {
                throw new RestApiException(ErrorCode.VALIDATE_FAIL);
            }
        }
        if (request.getCcspResultCode() != null) {
            if (!Pattern.matches(pattern1, request.getCcspResultCode()) && !Pattern.matches(pattern2, request.getCcspResultCode())
                    && !Pattern.matches(pattern3, request.getCcspResultCode())) {
                throw new RestApiException(ErrorCode.VALIDATE_FAIL);
            }
        }
        ServiceProgram entitySave = serviceProgramRepository.save(new ServiceProgram(request, servicePackage));
        //L??u sub_service_program
        SubServiceProgram subServiceProgram = new SubServiceProgram();
        subServiceProgram.setSubServiceProgramId(null);
        if (entitySave.getProgramId() != null && entitySave.getServicePackage().getPackageId() != null) {
            subServiceProgram.setProgramId(entitySave.getProgramId());
            subServiceProgram.setPackageId(entitySave.getServicePackage().getPackageId());
        }
        subServiceProgram.setMaxPackageExclude(request.getMaxPackageExclude());
        subServiceProgram.setMaxPcrfServiceExclude(request.getMaxPcrfServiceExclude());
        subServiceProgram.setMaxPackageGroupExclude(request.getMaxPackageGroupExclude());
        if (request.getFlexSubProgramId() != null) {
            subServiceProgram.setFlexSubProgramId(request.getFlexSubProgramId());
            subServiceProgram.setFlexFilterBundle(request.getFlexFilterBundle());
            subServiceProgram.setFlexMinQty(request.getFlexMinQty());
        }
        subServiceProgram.setIsDkRetry(request.getIsDKRetry());
        subServiceProgram.setIsCancel(request.getIsCancel());
        subServiceProgram.setIsInsert(request.getIsInsert());
        if (request.getRegisterNumberDay() != null) {
            subServiceProgram.setRegisterNumberDay(request.getRegisterNumberDay());
        } else {
            subServiceProgram.setRegisterNumberDay(0L);
        }
        if (request.getRenewNumberDay() != null) {
            subServiceProgram.setRenewNumberDay(request.getRenewNumberDay());
        } else {
            subServiceProgram.setRenewNumberDay(0L);
        }
        if(request.getSaleChargePrice()!=null){
            subServiceProgram.setSaleChargePrice(request.getSaleChargePrice());
        }else {
            subServiceProgram.setSaleChargePrice("0");
        }
        subServiceProgramRepository.save(subServiceProgram);
        return entitySave;
    }

    public ServiceProgram findById(Long id) {
        return serviceProgramRepository.findByProgramId(id);
    }

    public ServiceProgramResponse getDetail(Long id) {
        ServiceProgramResponse serviceProgramResponse = new ServiceProgramResponse();
        ServiceProgram serviceProgram = serviceProgramRepository.findByProgramId(id);
        SubServiceProgram subServiceProgram = subServiceProgramRepository.findByProgramId(id);
        serviceProgramResponse.setProgramId(serviceProgram.getProgramId());
        serviceProgramResponse.setPackageId(serviceProgram.getServicePackage().getPackageId());
        serviceProgramResponse.setPackageCode(serviceProgram.getServicePackage().getCode());
        serviceProgramResponse.setChargePrice(serviceProgram.getChargePrice());
        serviceProgramResponse.setIsMinusIn(serviceProgram.getIsNinusIn());
        serviceProgramResponse.setChargeTime(serviceProgram.getChargeTime());
        serviceProgramResponse.setAutoExtend(serviceProgram.getAutoExtend());
        serviceProgramResponse.setNumExtend(serviceProgram.getNumExtend());
        serviceProgramResponse.setVnptPckCode(serviceProgram.getVnptPckCode());
        serviceProgramResponse.setVnptPromCode(serviceProgram.getVnptPromCode());
        serviceProgramResponse.setDescription(serviceProgram.getDescription());
        serviceProgramResponse.setChargeType(serviceProgram.getChargeType());
        serviceProgramResponse.setMinusMethod(serviceProgram.getMinusMethod());
        serviceProgramResponse.setStaDate(Objects.nonNull(serviceProgram.getStaDate())
                                          ? DateTimeUtil.formatInstant(serviceProgram.getStaDate(), "dd/MM/yyyy HH:mm:ss")
                                          : "");
        serviceProgramResponse.setEndDate(Objects.nonNull(serviceProgram.getEndDate())
                                          ? DateTimeUtil.formatInstant(serviceProgram.getEndDate(), "dd/MM/yyyy HH:mm:ss")
                                          : "");
        serviceProgramResponse.setMinStepMinus(serviceProgram.getMinStepMinus());
        serviceProgramResponse.setCheckStepType(serviceProgram.getCheckStepType());
        serviceProgramResponse.setProgramCode(serviceProgram.getProgramCode());
        serviceProgramResponse.setAllowIsdnStatus(serviceProgram.getAllowIsdnStatus());
        serviceProgramResponse.setCcspServiceCode(serviceProgram.getCcspServiceCode());
        serviceProgramResponse.setCcspResultCode(serviceProgram.getCcspResultCode());
        serviceProgramResponse.setNumber1(serviceProgram.getNumber1());
        serviceProgramResponse.setNumber2(serviceProgram.getNumber2());
        serviceProgramResponse.setTotalUnit(serviceProgram.getTotalUnit());
        serviceProgramResponse.setDateBeforeRenew(serviceProgram.getDateBeforeRenew());
        serviceProgramResponse.setPackageIdNext(serviceProgram.getPackageIdNext());
        serviceProgramResponse.setProgramIdNext(serviceProgram.getProgramIdNext());
        serviceProgramResponse.setMsgBeforeRenew(serviceProgram.getMsgBeforeRenew());
        serviceProgramResponse.setIsDefaultProgram(serviceProgram.getIsDefaultProgram());
        serviceProgramResponse.setIsOnKtPro(serviceProgram.getIsOnKtPro());
        serviceProgramResponse.setExpireByOldPackage(serviceProgram.getExpireByOldPackage());
        serviceProgramResponse.setIsCalculateExpireDate(serviceProgram.getIsCalculateExpireDate());
        serviceProgramResponse.setMsgBeforeRenewEn(serviceProgram.getMsgBeforeRenewEn());
        if (subServiceProgram != null) {
            serviceProgramResponse.setMaxPcrfServiceExclude(subServiceProgram.getMaxPcrfServiceExclude());
            serviceProgramResponse.setMaxPackageExclude(subServiceProgram.getMaxPackageExclude());
            serviceProgramResponse.setMaxPackageGroupExclude(subServiceProgram.getMaxPackageGroupExclude());
            serviceProgramResponse.setFlexSubProgramId(subServiceProgram.getFlexSubProgramId());
            serviceProgramResponse.setFlexFilterBundle(subServiceProgram.getFlexFilterBundle());
            serviceProgramResponse.setFlexMinQty(subServiceProgram.getFlexMinQty());
            serviceProgramResponse.setIsDKRetry(subServiceProgram.getIsDkRetry());
            serviceProgramResponse.setIsCancel(subServiceProgram.getIsCancel());
            serviceProgramResponse.setIsInsert(subServiceProgram.getIsInsert());
            if (subServiceProgram.getRegisterNumberDay() != null && subServiceProgram.getRegisterNumberDay() > 0) {
                serviceProgramResponse.setHasRegisterNumberDay(true);
                serviceProgramResponse.setRegisterNumberDay(subServiceProgram.getRegisterNumberDay());
            } else {
                serviceProgramResponse.setHasRegisterNumberDay(false);
                serviceProgramResponse.setRegisterNumberDay(0L);
            }
            if (subServiceProgram.getRenewNumberDay() != null && subServiceProgram.getRenewNumberDay() > 0) {
                serviceProgramResponse.setHasRenewNumberDay(true);
                serviceProgramResponse.setRenewNumberDay(subServiceProgram.getRenewNumberDay());
            } else {
                serviceProgramResponse.setHasRenewNumberDay(false);
                serviceProgramResponse.setRenewNumberDay(0L);
            }
            if(subServiceProgram.getSaleChargePrice()!=null){
                serviceProgramResponse.setSaleChargePrice(subServiceProgram.getSaleChargePrice());
            }else {
                serviceProgramResponse.setSaleChargePrice("0");
            }
        }
        return serviceProgramResponse;
    }

    public ServiceProgram update(AddServiceProgramRequest request) {
        ServicePackage servicePackage =
                servicePackageRepository.findByCode(request.getPackageCode()).get();
        ServiceProgram serviceProgram = new ServiceProgram(request, servicePackage);
        // T??m ra t???t c??? serviceProgram tr??ng program_code
        List<ServiceProgram> listFindByProgramCode = serviceProgramRepository.findByProgramIdAndProgramCode(request.getServiceProgramId(), request.getProgramCode());
        if (listFindByProgramCode.size() > 0) {
            for (ServiceProgram item : listFindByProgramCode) {
                // Check 2 service program c?? tr??ng l???p th???i gian hay kh??ng?
                if (areTwoDateTimeRangesOverlapping(serviceProgram, item)) {
                    // N???u 1 tr?????ng h???p tr??ng l?? lo???i b??? lu??n.
                    // N???u t???t c??? tr?????ng h???p ?????u kh??ng tr??ng th?? m???i save
                    throw new RestApiException(ErrorCode.DUPLICATE_PROGRAM_CODE);
                }
            }
        }
        // T??m ra t???t c??? command_alias li??n quan ?????n service_program
        List<MapCommandAlias> listMapCommandAlias = mapCommandAliasRepository.findByProgramId(request.getServiceProgramId());
        for (MapCommandAlias mapCommandAlias : listMapCommandAlias) {
            // T??m ra t???t c??? serviceProgram tr??ng smsMo
            List<ServiceProgram> listServiceProgram = mapCommandAliasRepository.findBySmsMoAndCmdAliasId(mapCommandAlias.getSmsMo(), mapCommandAlias.getCmdAliasId());
            // Check tr??ng kho???ng th???i gian
            if (listServiceProgram.size() > 0) {
                for (ServiceProgram item : listServiceProgram) {
                    // Check 2 service program c?? tr??ng l???p th???i gian hay kh??ng?
                    if (areTwoDateTimeRangesOverlapping(serviceProgram, item)) {
                        // N???u 1 tr?????ng h???p tr??ng l?? lo???i b??? lu??n.
                        // N???u t???t c??? tr?????ng h???p ?????u kh??ng tr??ng th?? m???i save
                        throw new RestApiException(ErrorCode.DUPLICATE_SMS_MO);
                    }
                }
            }
        }
        // Check DEFAULT_PROGRAM
        if (request.getIsDefaultProgram()) {
            List<ServiceProgram> listFindDefaultProgram = serviceProgramRepository.findDefaultProgram(request.getServicePackageId());
            if (listFindDefaultProgram.size() > 0) {
                for (ServiceProgram item : listFindDefaultProgram) {
                    item.setIsDefaultProgram(false);
                    serviceProgramRepository.save(item);
                }
            }
        }
        if (request.getProgramCode().trim().equals("")) {
            request.setProgramCode(null);
        }
        ServiceProgram servicePr = findById(request.getServiceProgramId());
        String pattern1 = "([A-Za-z0-9]+(,*[A-Za-z0-9]+)*)";
        String pattern2 = "([A-Za-z0-9]+(\\s*[A-Za-z0-9]+)*)";
        String pattern3 = "([A-Za-z0-9]+(#*[A-Za-z0-9]+)*)";
        if (request.getCcspServiceCode() != null) {
            if (!Pattern.matches(pattern1, request.getCcspServiceCode()) && !Pattern.matches(pattern2, request.getCcspServiceCode())
                    && !Pattern.matches(pattern3, request.getCcspServiceCode())) {
                throw new RestApiException(ErrorCode.VALIDATE_FAIL);
            }
        }
        if (request.getCcspResultCode() != null) {
            if (!Pattern.matches(pattern1, request.getCcspResultCode()) && !Pattern.matches(pattern2, request.getCcspResultCode())
                    && !Pattern.matches(pattern3, request.getCcspResultCode())) {
                throw new RestApiException(ErrorCode.VALIDATE_FAIL);
            }
        }
        if (Objects.nonNull(servicePr)) {
            servicePr.setServicePackage(servicePackage);
            servicePr.setChargePrice(request.getChargePrice());
            servicePr.setIsNinusIn(request.getIsMinusIn());
            servicePr.setChargeTime(request.getChargeTime());
            servicePr.setAutoExtend(request.getAutoExtend());
            servicePr.setNumExtend(request.getNumExtend());
            servicePr.setVnptPromCode(request.getVnptPromCode());
            servicePr.setVnptPckCode(request.getVnptPckCode());
            servicePr.setChargeType(request.getChargeType());
            servicePr.setMinusMethod(request.getMinusMethod());
            servicePr.setStaDate(
                    DateTimeUtil.convertStringToInstant(request.getStaDate(), "dd/MM/yyyy HH:mm:ss"));
//            servicePr.setExtendEndDate(
//                    DateTimeUtil.convertStringToInstant(request.getExtendEndDate(), "dd/MM/yyyy HH:mm:ss"));
            servicePr.setEndDate(
                    DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss"));
            servicePr.setDescription(request.getDescription());
            servicePr.setMinStepMinus(request.getMinStepMinus());
            servicePr.setCheckStepType(request.getCheckStepType());
            servicePr.setProgramCode(request.getProgramCode());
            servicePr.setAllowIsdnStatus(request.getAllowIsdnStatus());
            if (request.getCcspServiceCode() != null) {
                servicePr.setCcspServiceCode(this.convertToCcspDesign(request.getCcspServiceCode()));
            }
            if (request.getCcspResultCode() != null) {
                servicePr.setCcspResultCode(this.convertToCcspDesign(request.getCcspResultCode()));
            }
            servicePr.setNumber1(request.getNumber1());
            servicePr.setNumber2(request.getNumber2());
            servicePr.setTotalUnit(request.getTotalUnit());
            servicePr.setDateBeforeRenew(request.getDateBeforeRenew());
            servicePr.setPackageIdNext(request.getPackageIdNext());
            servicePr.setProgramIdNext(request.getProgramIdNext());
            servicePr.setMsgBeforeRenew(request.getMsgBeforeRenew());
            servicePr.setIsDefaultProgram(request.getIsDefaultProgram());
            servicePr.setIsOnKtPro(request.getIsOnKtPro());
            servicePr.setExpireByOldPackage(request.getExpireByOldPackage());
            //Th??m HSD ri??ng cho roaming
            servicePr.setIsCalculateExpireDate(request.getIsCalculateExpireDate());
            //Th??m ng??n ng??? cho tin nh???n tr?????c gia h???n
            servicePr.setMsgBeforeRenewEn(request.getMsgBeforeRenewEn());
            SubServiceProgram subServiceProgramFromDb =
                    subServiceProgramRepository.findByProgramId(servicePr.getProgramId());
            if (subServiceProgramFromDb != null) {
                if (request.getMaxPcrfServiceExclude() != null && request.getMaxPackageExclude() != null) {
                    subServiceProgramRepository.updateMaxByProgramId(servicePr.getProgramId(),
                                                                     request.getMaxPcrfServiceExclude(),
                                                                     request.getMaxPackageExclude(), request.getMaxPackageGroupExclude());
                }
                if (request.getFlexSubProgramId() != null) {
                    subServiceProgramRepository.updateFlexByProgramId(servicePr.getProgramId(),
                                                                      request.getFlexSubProgramId(),
                                                                      request.getFlexFilterBundle(), request.getFlexMinQty());
                }
                subServiceProgramRepository.updateByProgramId(servicePr.getProgramId(), request.getIsDKRetry(),
                                                              request.getIsCancel(), request.getIsInsert());
                if (request.getHasRegisterNumberDay() || request.getHasRenewNumberDay()) {
                    if (!request.getHasRenewNumberDay() && request.getHasRegisterNumberDay()) {
                        subServiceProgramRepository.updateWithProgramId(servicePr.getProgramId(),
                                                                        request.getRegisterNumberDay(), 0L);
                    } else if (request.getHasRenewNumberDay() && !request.getHasRegisterNumberDay()) {
                        subServiceProgramRepository.updateWithProgramId(servicePr.getProgramId(),
                                                                        0L, request.getRenewNumberDay());
                    } else {
                        subServiceProgramRepository.updateWithProgramId(servicePr.getProgramId(),
                                                                        request.getRegisterNumberDay(), request.getRenewNumberDay());
                    }
                } else {
                    subServiceProgramRepository.updateWithProgramId(servicePr.getProgramId(), 0L, 0L);
                }
                if(request.getSaleChargePrice()!=null){
                    subServiceProgramRepository.updateSaleCharge(servicePr.getProgramId(), request.getSaleChargePrice());
                }else {
                    subServiceProgramRepository.updateSaleCharge(servicePr.getProgramId(),"0");
                }
            } else {
                //L??u sub_service_program
                SubServiceProgram subServiceProgram = new SubServiceProgram();
                subServiceProgram.setSubServiceProgramId(null);
                subServiceProgram.setProgramId(servicePr.getProgramId());
                subServiceProgram.setPackageId(servicePr.getServicePackage().getPackageId());
                subServiceProgram.setMaxPackageExclude(request.getMaxPackageExclude());
                subServiceProgram.setMaxPcrfServiceExclude(request.getMaxPcrfServiceExclude());
                subServiceProgram.setMaxPackageGroupExclude(request.getMaxPackageGroupExclude());
                subServiceProgram.setIsDkRetry(request.getIsDKRetry());
                subServiceProgram.setIsCancel(request.getIsCancel());
                subServiceProgram.setIsInsert(request.getIsInsert());
                if (request.getRegisterNumberDay() != null) {
                    subServiceProgram.setRegisterNumberDay(request.getRegisterNumberDay());
                } else {
                    subServiceProgram.setRegisterNumberDay(0L);
                }
                if (request.getRenewNumberDay() != null) {
                    subServiceProgram.setRenewNumberDay(request.getRenewNumberDay());
                } else {
                    subServiceProgram.setRenewNumberDay(0L);
                }
                if(request.getSaleChargePrice()!=null){
                    subServiceProgram.setSaleChargePrice(subServiceProgram.getSaleChargePrice());
                }else {
                    subServiceProgram.setSaleChargePrice("0");
                }
                subServiceProgramRepository.save(subServiceProgram);
            }
            return serviceProgramRepository.save(servicePr);
        }
        throw new RestApiException(ErrorCode.UPDATE_SERVICE_PROGRAM_FAILED);
    }

    public DetailServicePackageResponse findByPackageId(Long packageId, Pageable pageable) {
        ServicePackage entity = servicePackageRepository.findById(packageId)
                                                        .orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));
        ServicePackageResponse response = new ServicePackageResponse(entity);
        Page<ServiceProgram> listPage = serviceProgramRepository.findByPackageId(packageId, pageable);
        Page<ServiceProgramResponse> pageResponse = listPage.map(service -> getDetail(service.getProgramId()));
        ;
        return new DetailServicePackageResponse(response, pageResponse);
    }

    public List<ServiceProgram> findByPackageIdWithoutPageable(Long packageId) {
        ServicePackage entity = servicePackageRepository.findById(packageId)
                                                        .orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));
        List<ServiceProgram> serviceProgramList = serviceProgramRepository.findByPackageIdWithoutPageable(packageId);
        return serviceProgramList;
    }

    public void updateCheckMaxRegisted(AddServiceProgramRequest request) {
        ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getServiceProgramId()).orElse(null);
        if (serviceProgram == null) {
            throw new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND);
        } else {
            serviceProgram.setNumber1(request.getNumber1());
            serviceProgram.setNumber2(request.getNumber2());
            serviceProgramRepository.save(serviceProgram);
        }
    }

    public List<ServiceProgram> findAllByPackageId(Long packageId) {
        return serviceProgramRepository.findAllByPackageId(packageId);
    }

    public DetailServiceProgramResponse detailServiceProgram(Long programId, Pageable pageableIN,
                                                             Pageable pageableBILLING, Pageable pageablePCRF,
                                                             Pageable pageableTransaction, Pageable pageCcspInfo) {
        ServiceProgram serviceProgram = findById(programId);
        ServiceProgramResponse serviceProgramResponse = new ServiceProgramResponse(serviceProgram);
        Page<BucketsInfo> pageIn = bucketsInfoRepository.findAllByProgramId(programId, pageableIN);
        Page<BucketsInfoResponse> pageInRes =
                pageIn.map(new Function<BucketsInfo, BucketsInfoResponse>() {
                    @Override
                    public BucketsInfoResponse apply(BucketsInfo t) {
                        return new BucketsInfoResponse(t);
                    }
                });
        Page<MapServicePackage> pageBilling =
                mapServicePackageRepository.findAllByProgramId(programId, pageableBILLING);
        Page<MapServicePackageResponse> pageBillingRes =
                pageBilling.map(new Function<MapServicePackage, MapServicePackageResponse>() {

                    @Override
                    public MapServicePackageResponse apply(MapServicePackage t) {
                        return new MapServicePackageResponse(t);
                    }
                });
        Page<NdsTypeParamProgram> pagePCRF =
                ndsTypeParamProgramRepository.findAllByProgramId(programId, pageablePCRF);
        Page<NdsTypeParamProgramResponse> pagePCRFRes =
                pagePCRF.map(new Function<NdsTypeParamProgram, NdsTypeParamProgramResponse>() {

                    @Override
                    public NdsTypeParamProgramResponse apply(NdsTypeParamProgram t) {
                        return new NdsTypeParamProgramResponse(t);
                    }
                });
        Page<MapCommandAlias> pageTransaction = mapCommandAliasRepository.findAllByProgramId(programId, pageableTransaction);
        Page<MapCommandAliasResponse> pageTransactionRes =
                pageTransaction.map(new Function<MapCommandAlias, MapCommandAliasResponse>() {

                    @Override
                    public MapCommandAliasResponse apply(MapCommandAlias t) {
                        return new MapCommandAliasResponse(t);
                    }
                });
        List<MinusMoney> listMinusMoney = minusMoneyRepository.findAllByProgramId(programId);
//        Page<ServiceInfo> pageService =
//                serviceInfoRepository.findAllByProgramId(programId, pageServiceInfo);
        Page<CcspInfo> pageCcsp = ccspInfoRepository.findAllByProgramId(programId, pageCcspInfo);
        Page<CcspInfoResponse> pageCcspInfoRes =
                pageCcsp.map(new Function<CcspInfo, CcspInfoResponse>() {

                    @Override
                    public CcspInfoResponse apply(CcspInfo t) {
                        return new CcspInfoResponse(t);
                    }
                });

//        Page<ServiceInfoResponse> pageServiceRes =
//                pageService.map(new Function<ServiceInfo, ServiceInfoResponse>() {
//                    @Override
//                    public ServiceInfoResponse apply(ServiceInfo t) {
//                        return new ServiceInfoResponse(t);
//                    }
//                });
        return new DetailServiceProgramResponse(serviceProgramResponse, pageInRes, pageBillingRes,
                                                pagePCRFRes, pageTransactionRes, listMinusMoney, pageCcspInfoRes);
    }


    public List<ActioncodeMapping> getAllActioncodeMapping() {
        return actioncodeMappingRepository.findAll();
    }

    //	Convert A#B#C
    public String convertToCcspDesign(String str) {
        String returnStr = "";
        if (str.indexOf(",") > 0) {
            returnStr = str.replaceAll(",", "#").toUpperCase();
        } else if (str.indexOf(" ") > 0) {
            returnStr = str.replaceAll(" ", "#").toUpperCase();
        } else if (str.indexOf("#") > 0) {
            returnStr = str.toUpperCase();
        } else {
            returnStr = str;
        }
        return returnStr;
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiResponse cloneOneServiceProgram(Long newPackageId, Long oldPackageId, ServicePackage service,
                                              ServiceProgram oldProgram) {
        ApiResponse response = new ApiResponse();
        // 3 Clone ch????ng tr??nh
        ServiceProgram newProgram = cloneServiceProgram(oldProgram, newPackageId);
        // T???o Log Action
        LogAction logAction = new LogAction();
        logAction.setTableAction("service_program");
        logAction.setAccount(tokenProvider.account);
        logAction.setAction("CREATE");
        logAction.setOldValue(null);
        logAction.setNewValue(newProgram.toString());
        logAction.setTimeAction(new Date());
        logAction.setIdAction(newProgram.getProgramId());
        logActionService.add(logAction);

        SubServiceProgram subServiceProgramFromDb =
                subServiceProgramRepository.findByProgramId(oldProgram.getProgramId());

        //L??u sub_service_program
        SubServiceProgram subServiceProgram = new SubServiceProgram();
        subServiceProgram.setSubServiceProgramId(null);
        subServiceProgram.setProgramId(newProgram.getProgramId());
        subServiceProgram.setPackageId(newProgram.getServicePackage().getPackageId());
        if (subServiceProgramFromDb != null) {
            subServiceProgram.setMaxPackageExclude(subServiceProgramFromDb.getMaxPackageExclude());
            subServiceProgram.setMaxPcrfServiceExclude(subServiceProgramFromDb.getMaxPcrfServiceExclude());
            subServiceProgram.setMaxPackageGroupExclude(subServiceProgramFromDb.getMaxPackageGroupExclude());
            subServiceProgram.setFlexSubProgramId(subServiceProgramFromDb.getFlexSubProgramId());
            subServiceProgram.setFlexFilterBundle(subServiceProgramFromDb.getFlexFilterBundle());
            subServiceProgram.setFlexMinQty(subServiceProgramFromDb.getFlexMinQty());
            subServiceProgram.setIsDkRetry(subServiceProgramFromDb.getIsDkRetry());
            subServiceProgram.setIsCancel(subServiceProgramFromDb.getIsCancel());
            subServiceProgram.setIsInsert(subServiceProgramFromDb.getIsInsert());
            if (subServiceProgramFromDb.getRegisterNumberDay() != null) {
                subServiceProgram.setRegisterNumberDay(subServiceProgramFromDb.getRegisterNumberDay());
            } else {
                subServiceProgram.setRegisterNumberDay(0L);
            }
            if (subServiceProgramFromDb.getRenewNumberDay() != null) {
                subServiceProgram.setRenewNumberDay(subServiceProgramFromDb.getRenewNumberDay());
            } else {
                subServiceProgram.setRenewNumberDay(0L);
            }
            if(subServiceProgramFromDb.getSaleChargePrice()!=null){
                subServiceProgram.setSaleChargePrice(subServiceProgramFromDb.getSaleChargePrice());
            }else {
                subServiceProgram.setSaleChargePrice("0");
            }
        }
        subServiceProgramRepository.save(subServiceProgram);

        // 3.1 T??m nh???ng th??ng tin ?????u n???i IN c???a g??i c?????c v?? ch????ng tr??nh c??
        List<BucketsInfo> lstOldBucketInfos = bucketsInfoRepository.findByPackageIdAndProgramId(oldPackageId, oldProgram.getProgramId());
        if (lstOldBucketInfos.size() > 0) {
            // 3.1.2 Th???c hi???n clone th??ng tin ?????u n???i IN n???u c??
            for (BucketsInfo bi : lstOldBucketInfos) {
                cloneBucketInfo(bi, newPackageId, newProgram);
            }
        }
        // 3.2 T??m nh???ng th??ng tin ?????u n???i BILLING c???a c??c
        List<MapServicePackage> lstOldMapServicePackages = mapServicePackageRepository.findByPackageIdAndProgramId(oldPackageId, oldProgram.getProgramId());
        if (lstOldBucketInfos.size() > 0) {
            // 3.1.2 Th???c hi???n clone th??ng tin ?????u n???i BILLING n???u c??
            for (MapServicePackage msp : lstOldMapServicePackages) {
                cloneMapServicePackage(msp, newPackageId, newProgram);
            }
        }
        // 3.3 T??m nh???ng th??ng tin ?????u n???i PCRF
        List<NdsTypeParamProgram> lstOldNdsTypeParamPrograms = ndsTypeParamProgramRepository.findByPackageIdAndProgramId(oldPackageId, oldProgram.getProgramId());
        // 3.1.2 Th???c hi???n clone th??ng tin ?????u n???i PCRF n???u c??
        if (lstOldNdsTypeParamPrograms.size() > 0) {
            for (NdsTypeParamProgram ntpp : lstOldNdsTypeParamPrograms) {
                cloneNdsTypeParamProgram(ntpp, service, newProgram);
            }
        }
        // 3.4 Clone Transcode
        // Kh??ng clone transcode do tr??ng smsMo
        // 3.5 Clone th??ng tin b??? sung (Service_info)
        List<ServiceInfo> listServiceInfo = serviceInfoRepository.findAllByPackageId(oldPackageId, oldProgram.getProgramId());
        if (listServiceInfo.size() > 0) {
            for (ServiceInfo serviceInfo : listServiceInfo) {
                cloneServiceInfo(serviceInfo, service, newProgram);
            }
        }
        // RESPONSE
        response.setStatus(ApiResponseStatus.SUCCESS.getValue());
        response.setData(newProgram);
        response.setMessage("Clone th??ng tin ch????ng tr??nh th??nh c??ng");
        return response;
    }

    @SneakyThrows(CloneNotSupportedException.class)
    private ServiceProgram cloneServiceProgram(ServiceProgram oldProgram, Long newId) {
        ServiceProgram serviceProgram = (ServiceProgram) oldProgram.clone();
        serviceProgram.setProgramId(null);
        serviceProgram.setServicePackage(servicePackageRepository.findByPackageId(newId));
        if (oldProgram.getProgramCode() != null)
            serviceProgram.setProgramCode(oldProgram.getProgramCode() + "_copy_" + System.currentTimeMillis());
        // a H???i MBF confirm 04/03/2022
        // a H???i MBF confirm 05/03/2022
        serviceProgram.setCommandId(oldProgram.getProgramId() + 1000000);
        serviceProgramRepository.save(serviceProgram);
        return serviceProgram;
    }

    @SneakyThrows(CloneNotSupportedException.class)
    private void cloneBucketInfo(BucketsInfo bi, Long newPackageId, ServiceProgram newProgram) {
        BucketsInfo bucketsInfo = (BucketsInfo) bi.clone();
        bucketsInfo.setBucketsId(null);
        bucketsInfo.setPackageId(newPackageId);
        bucketsInfo.setServiceProgram(newProgram);
        BucketsInfo bucketsInfoReturn = bucketsInfoRepository.saveAndFlush(bucketsInfo);
        LogAction logAction = new LogAction();
        logAction.setAction("CREATE");
        logAction.setOldValue(null);
        logAction.setNewValue(bucketsInfoReturn.toString());
        logAction.setTimeAction(new Date());
        logAction.setIdAction(bucketsInfoReturn.getBucketsId());
        logAction.setAccount(tokenProvider.account);
        logAction.setTableAction("buckets_info");
        logActionService.add(logAction);
    }

    @SneakyThrows(CloneNotSupportedException.class)
    private void cloneMapServicePackage(MapServicePackage msp, Long newPackageId, ServiceProgram newProgram) {
        MapServicePackage mapServicePackage = (MapServicePackage) msp.clone();
        Long maxId = mapServicePackageRepository.findIdMax();
        mapServicePackage.setMapId(maxId + 1);
        mapServicePackage.setPackageId(newPackageId);
        mapServicePackage.setServiceProgram(newProgram);
        MapServicePackage mapServicePackageReturn = mapServicePackageRepository.saveAndFlush(mapServicePackage);
        LogAction logAction = new LogAction();
        logAction.setAction("CREATE");
        logAction.setOldValue(null);
        logAction.setNewValue(mapServicePackageReturn.toString());
        logAction.setTimeAction(new Date());
        logAction.setIdAction(mapServicePackageReturn.getMapId());
        logAction.setAccount(tokenProvider.account);
        logAction.setTableAction("map_service_package");
        logActionService.add(logAction);
    }

    @SneakyThrows(CloneNotSupportedException.class)
    private void cloneNdsTypeParamProgram(NdsTypeParamProgram ntpp, ServicePackage newPackage,
                                          ServiceProgram newProgram) {
        NdsTypeParamProgram ndsTypeParamProgram = (NdsTypeParamProgram) ntpp.clone();
        ndsTypeParamProgram.setNdsTypeParamKey(null);
        ndsTypeParamProgram.setServicePackage(newPackage);
        ndsTypeParamProgram.setServiceProgram(newProgram);
        NdsTypeParamProgram ndsTypeParamProgramReturn = ndsTypeParamProgramRepository.saveAndFlush(ndsTypeParamProgram);
        LogAction logAction = new LogAction();
        logAction.setAction("CREATE");
        logAction.setOldValue(null);
        logAction.setNewValue(ndsTypeParamProgramReturn.toString());
        logAction.setTimeAction(new Date());
        logAction.setIdAction(ndsTypeParamProgramReturn.getNdsTypeParamKey());
        logAction.setAccount(tokenProvider.account);
        logAction.setTableAction("nds_type_param_program");
        logActionService.add(logAction);
    }

    @SneakyThrows(CloneNotSupportedException.class)
    private void cloneServiceInfo(ServiceInfo si, ServicePackage newPackage, ServiceProgram newProgram) {
        ServiceInfo serviceInfo = (ServiceInfo) si.clone();
        serviceInfo.setServiceInfoId(null);
        serviceInfo.setPackageId(newPackage.getPackageId());
        serviceInfo.setServiceProgram(newProgram);
        ServiceInfo serviceInfoReturn = serviceInfoRepository.saveAndFlush(serviceInfo);
        LogAction logAction = new LogAction();
        logAction.setAction("CREATE");
        logAction.setOldValue(null);
        logAction.setNewValue(serviceInfoReturn.toString());
        logAction.setTimeAction(new Date());
        logAction.setIdAction(serviceInfoReturn.getServiceInfoId());
        logAction.setAccount(tokenProvider.account);
        logAction.setTableAction("service_info");
        logActionService.add(logAction);
    }

    public void delete(ServiceProgram serviceProgram) {
        // T???o Log Action
        LogAction logAction = new LogAction();
        logAction.setTableAction("service_program");
        logAction.setAccount(tokenProvider.account);
        logAction.setAction("DELETE");
        logAction.setOldValue(serviceProgram.toString());
        logAction.setNewValue(null);
        logAction.setTimeAction(new Date());
        logAction.setIdAction(serviceProgram.getProgramId());
        logActionService.add(logAction);
        // X??a c??c th??nh ph???n li??n quan
        // Buckets_info
        List<BucketsInfo> listBucketsInfo = bucketsInfoRepository.findByPackageIdAndProgramId(serviceProgram.getServicePackage()
                                                                                                            .getPackageId(), serviceProgram.getProgramId());
        for (BucketsInfo bucketsInfo : listBucketsInfo) {
            // T???o Log Action
            LogAction logActionBucketsInfo = new LogAction();
            logActionBucketsInfo.setTableAction("buckets_info");
            logActionBucketsInfo.setAccount(tokenProvider.account);
            logActionBucketsInfo.setAction("DELETE");
            logActionBucketsInfo.setOldValue(bucketsInfo.toString());
            logActionBucketsInfo.setNewValue(null);
            logActionBucketsInfo.setTimeAction(new Date());
            logActionBucketsInfo.setIdAction(bucketsInfo.getBucketsId());
            logActionService.add(logActionBucketsInfo);
            //
            bucketsInfoRepository.delete(bucketsInfo);
        }
        // MapServicePackage
        List<MapServicePackage> listMapServicePackage = mapServicePackageRepository.findByPackageIdAndProgramId(
                serviceProgram.getServicePackage().getPackageId(), serviceProgram.getProgramId());
        for (MapServicePackage mapServicePackage : listMapServicePackage) {
            // T???o Log Action
            LogAction logActionMapServicePackage = new LogAction();
            logActionMapServicePackage.setTableAction("map_service_package");
            logActionMapServicePackage.setAccount(tokenProvider.account);
            logActionMapServicePackage.setAction("DELETE");
            logActionMapServicePackage.setOldValue(mapServicePackage.toString());
            logActionMapServicePackage.setNewValue(null);
            logActionMapServicePackage.setTimeAction(new Date());
            logActionMapServicePackage.setIdAction(mapServicePackage.getMapId());
            logActionService.add(logActionMapServicePackage);
            //
            mapServicePackageRepository.delete(mapServicePackage);
        }

        // NdsTypeParamProgram
        List<NdsTypeParamProgram> listNdsTypeParamProgram = ndsTypeParamProgramRepository.findByPackageIdAndProgramId(
                serviceProgram.getServicePackage().getPackageId(), serviceProgram.getProgramId());
        for (NdsTypeParamProgram ndsTypeParamProgram : listNdsTypeParamProgram) {
            // T???o Log Action
            LogAction logActionNdsTypeParamProgram = new LogAction();
            logActionNdsTypeParamProgram.setTableAction("nds_type_param_program");
            logActionNdsTypeParamProgram.setAccount(tokenProvider.account);
            logActionNdsTypeParamProgram.setAction("DELETE");
            logActionNdsTypeParamProgram.setOldValue(ndsTypeParamProgram.toString());
            logActionNdsTypeParamProgram.setNewValue(null);
            logActionNdsTypeParamProgram.setTimeAction(new Date());
            logActionNdsTypeParamProgram.setIdAction(ndsTypeParamProgram.getNdsTypeParamKey());
            logActionService.add(logActionNdsTypeParamProgram);
            //
            ndsTypeParamProgramRepository.delete(ndsTypeParamProgram);
        }

        // MapCommandAlias
        List<MapCommandAlias> listMapCommandAlias = mapCommandAliasRepository.findByPackageIdAndProgramId(
                serviceProgram.getServicePackage().getPackageId(), serviceProgram.getProgramId());
        for (MapCommandAlias mapCommandAlias : listMapCommandAlias) {
            // T???o Log Action
            LogAction logActionMapCommandAlias = new LogAction();
            logActionMapCommandAlias.setTableAction("map_command_alias");
            logActionMapCommandAlias.setAccount(tokenProvider.account);
            logActionMapCommandAlias.setAction("DELETE");
            logActionMapCommandAlias.setOldValue(mapCommandAlias.toString());
            logActionMapCommandAlias.setNewValue(null);
            logActionMapCommandAlias.setTimeAction(new Date());
            logActionMapCommandAlias.setIdAction(mapCommandAlias.getCmdAliasId());
            logActionService.add(logActionMapCommandAlias);
            //
            mapCommandAliasRepository.delete(mapCommandAlias);
        }

        // MinusMoney
        List<MinusMoney> listMinusMoney = minusMoneyRepository.findByPackageIdAndProgramId(
                serviceProgram.getServicePackage().getPackageId(), serviceProgram.getProgramId());
        for (MinusMoney minusMoney : listMinusMoney) {
            // T???o Log Action
            LogAction logActionMinusMoney = new LogAction();
            logActionMinusMoney.setTableAction("minus_money_ladder");
            logActionMinusMoney.setAccount(tokenProvider.account);
            logActionMinusMoney.setAction("DELETE");
            logActionMinusMoney.setOldValue(minusMoney.toString());
            logActionMinusMoney.setNewValue(null);
            logActionMinusMoney.setTimeAction(new Date());
            logActionMinusMoney.setIdAction(minusMoney.getMinusMoneyLadderId());
            logActionService.add(logActionMinusMoney);
            //
            minusMoneyRepository.delete(minusMoney);
        }

        // ServiceInfo
        List<ServiceInfo> listServiceInfo = serviceInfoRepository.findByPackageIdAndProgramId(
                serviceProgram.getServicePackage().getPackageId(), serviceProgram.getProgramId());
        for (ServiceInfo serviceInfo : listServiceInfo) {
            // T???o Log Action
            LogAction logActionServiceInfo = new LogAction();
            logActionServiceInfo.setTableAction("service_info");
            logActionServiceInfo.setAccount(tokenProvider.account);
            logActionServiceInfo.setAction("DELETE");
            logActionServiceInfo.setOldValue(serviceInfo.toString());
            logActionServiceInfo.setNewValue(null);
            logActionServiceInfo.setTimeAction(new Date());
            logActionServiceInfo.setIdAction(serviceInfo.getServiceInfoId());
            logActionService.add(logActionServiceInfo);
            //
            serviceInfoRepository.delete(serviceInfo);
        }

        // SMS Respond
        List<MapSmsRespond> mapSmsRespondList = mapSmsRespondRepository.findAllByProgramId(serviceProgram.getProgramId());
        for (MapSmsRespond mapSmsRespond : mapSmsRespondList) {
            // T???o Log Action
            LogAction logActionServiceInfo = new LogAction();
            logActionServiceInfo.setTableAction("map_sms_respond");
            logActionServiceInfo.setAccount(tokenProvider.account);
            logActionServiceInfo.setAction("DELETE");
            logActionServiceInfo.setOldValue(mapSmsRespond.toString());
            logActionServiceInfo.setNewValue(null);
            logActionServiceInfo.setTimeAction(new Date());
            logActionServiceInfo.setIdAction(mapSmsRespond.getMapSmsRespondId());
            logActionService.add(logActionServiceInfo);
            //
            mapSmsRespondRepository.delete(mapSmsRespond);
        }

        subServiceProgramRepository.deleteByProgramId(serviceProgram.getProgramId());

        serviceProgramRepository.delete(serviceProgram);
    }

    public boolean areTwoDateTimeRangesOverlapping(ServiceProgram serviceProgramA, ServiceProgram serviceProgramB) {
        if (serviceProgramA.getEndDate() == null) {
            if (serviceProgramB.getEndDate() == null) {
                return true;
            } else {
                if (serviceProgramB.getEndDate().toEpochMilli() >= serviceProgramA.getStaDate().toEpochMilli()) {
                    return true;
                }
            }
        } else {
            if (serviceProgramB.getEndDate() == null) {
                if (serviceProgramA.getEndDate().toEpochMilli() >= serviceProgramB.getStaDate().toEpochMilli()) {
                    return true;
                }
            } else {
                if (serviceProgramA.getEndDate().toEpochMilli() >= serviceProgramB.getStaDate().toEpochMilli() &&
                        serviceProgramA.getStaDate().toEpochMilli() <= serviceProgramB.getEndDate().toEpochMilli()) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<ServiceProgramResponse> findAllProgramByPackageId(Long packageId) {
        List<Long> listProgramId = serviceProgramRepository.findAllProgramId(packageId);
        List<ServiceProgramResponse> listResponse = new ArrayList<>();
        for (Long id : listProgramId) {
            ServiceProgramResponse response = getDetail(id);
            listResponse.add(response);
        }
        return listResponse;
    }
}
