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

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

@Service
public class ServiceProgramService {

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

    public Page<ServiceProgram> findAll(String search, Pageable pageable) {
        if (StringUtils.hasLength(search))
            return serviceProgramRepository.findAll(search, pageable);
        return serviceProgramRepository.findAll(pageable);
    }

    public ServiceProgram add(AddServiceProgramRequest request) {
        ServicePackage servicePackage =
                servicePackageRepository.findByCode(request.getPackageCode()).get();
        ServiceProgram serviceProgram = new ServiceProgram(request, servicePackage);
        // Tìm ra tất cả serviceProgram trùng program_code
        List<ServiceProgram> listFindByProgramCode = serviceProgramRepository.findByProgramCode(request.getProgramCode());
        if (listFindByProgramCode.size() > 0) {
            for (ServiceProgram item: listFindByProgramCode) {
                // Check 2 service program có trùng lặp thời gian hay không?
                if (areTwoDateTimeRangesOverlapping(serviceProgram, item)) {
                    // Nếu 1 trường hợp trùng là loại bỏ luôn.
                    // Nếu tất cả trường hợp đều không trùng thì mới save
                    throw new RestApiException(ErrorCode.DUPLICATE_PROGRAM_CODE);
                }
            }
        }

        if (request.getProgramCode().trim().equals("")) {
            request.setProgramCode(null);
        }
        // Check DEFAULT_PROGRAM
        if (request.getIsDefaultProgram() == true){
            List<ServiceProgram> listFindDefaultProgram = serviceProgramRepository.findDefaultProgram(servicePackage.getPackageId());
            if (listFindDefaultProgram.size() > 0) {
                for (ServiceProgram item: listFindDefaultProgram) {
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
        return serviceProgramRepository.save(new ServiceProgram(request, servicePackage));
    }

    public ServiceProgram findById(Long id) {
        return serviceProgramRepository.findByProgramId(id);
    }

    public ServiceProgram update(AddServiceProgramRequest request) {
        ServicePackage servicePackage =
                servicePackageRepository.findByCode(request.getPackageCode()).get();
        ServiceProgram serviceProgram = new ServiceProgram(request, servicePackage);
        // Tìm ra tất cả serviceProgram trùng program_code
        List<ServiceProgram> listFindByProgramCode = serviceProgramRepository.findByProgramIdAndProgramCode(request.getServiceProgramId(), request.getProgramCode());
        if (listFindByProgramCode.size() > 0) {
            for (ServiceProgram item: listFindByProgramCode) {
                // Check 2 service program có trùng lặp thời gian hay không?
                if (areTwoDateTimeRangesOverlapping(serviceProgram, item)) {
                    // Nếu 1 trường hợp trùng là loại bỏ luôn.
                    // Nếu tất cả trường hợp đều không trùng thì mới save
                    throw new RestApiException(ErrorCode.DUPLICATE_PROGRAM_CODE);
                }
            }
        }
        // Tìm ra tất cả command_alias liên quan đến service_program
        List<MapCommandAlias> listMapCommandAlias = mapCommandAliasRepository.findByProgramId(request.getServiceProgramId());
        for (MapCommandAlias mapCommandAlias: listMapCommandAlias) {
            // Tìm ra tất cả serviceProgram trùng smsMo
            List<ServiceProgram> listServiceProgram = mapCommandAliasRepository.findBySmsMoAndCmdAliasId(mapCommandAlias.getSmsMo(), mapCommandAlias.getCmdAliasId());
            // Check trùng khoảng thời gian
            if (listServiceProgram.size() > 0) {
                for (ServiceProgram item: listServiceProgram) {
                    // Check 2 service program có trùng lặp thời gian hay không?
                    if (areTwoDateTimeRangesOverlapping(serviceProgram, item)) {
                        // Nếu 1 trường hợp trùng là loại bỏ luôn.
                        // Nếu tất cả trường hợp đều không trùng thì mới save
                        throw new RestApiException(ErrorCode.DUPLICATE_SMS_MO);
                    }
                }
            }
        }
        // Check DEFAULT_PROGRAM
        if (request.getIsDefaultProgram() == true){
            List<ServiceProgram> listFindDefaultProgram = serviceProgramRepository.findDefaultProgram(request.getServicePackageId());
            if (listFindDefaultProgram.size() > 0) {
                for (ServiceProgram item: listFindDefaultProgram) {
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
            //Thêm HSD riêng cho roaming
            servicePr.setIsCalculateExpireDate(request.getIsCalculateExpireDate());
            return serviceProgramRepository.save(servicePr);
        }
        throw new RestApiException(ErrorCode.UPDATE_SERVICE_PROGRAM_FAILED);
    }

    public DetailServicePackageResponse findByPackageId(Long packageId, Pageable pageable) {
        ServicePackage entity = servicePackageRepository.findById(packageId)
                .orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));
        ServicePackageResponse response = new ServicePackageResponse(entity);
        Page<ServiceProgram> listPage = serviceProgramRepository.findByPackageId(packageId, pageable);
        Page<ServiceProgramResponse> pageResponse = listPage.map(ServiceProgramResponse::new);
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
        Page<CcspInfo> pageCcsp =  ccspInfoRepository.findAllByProgramId(programId, pageCcspInfo);
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
    public ApiResponse cloneOneServiceProgram(Long newPackageId, Long oldPackageId, ServicePackage service, ServiceProgram oldProgram) {
        ApiResponse response = new ApiResponse();
        // 3 Clone chương trình
        ServiceProgram newProgram = cloneServiceProgram(oldProgram, newPackageId);
        // Tạo Log Action
        LogAction logAction = new LogAction();
        logAction.setTableAction("service_program");
        logAction.setAccount(tokenProvider.account);
        logAction.setAction("CREATE");
        logAction.setOldValue(null);
        logAction.setNewValue(newProgram.toString());
        logAction.setTimeAction(new Date());
        logAction.setIdAction(newProgram.getProgramId());
        logActionService.add(logAction);

        // 3.1 Tìm những thông tin đấu nối IN của gói cước và chương trình cũ
        List<BucketsInfo> lstOldBucketInfos = bucketsInfoRepository.findByPackageIdAndProgramId(oldPackageId, oldProgram.getProgramId());
        if (lstOldBucketInfos.size() > 0) {
            // 3.1.2 Thực hiện clone thông tin đấu nối IN nếu có
            for (BucketsInfo bi : lstOldBucketInfos) {
                cloneBucketInfo(bi, newPackageId, newProgram);
            }
        }
        // 3.2 Tìm những thông tin đấu nối BILLING của các
        List<MapServicePackage> lstOldMapServicePackages = mapServicePackageRepository.findByPackageIdAndProgramId(oldPackageId, oldProgram.getProgramId());
        if (lstOldBucketInfos.size() > 0) {
            // 3.1.2 Thực hiện clone thông tin đấu nối BILLING nếu có
            for (MapServicePackage msp : lstOldMapServicePackages) {
                cloneMapServicePackage(msp, newPackageId, newProgram);
            }
        }
        // 3.3 Tìm những thông tin đấu nối PCRF
        List<NdsTypeParamProgram> lstOldNdsTypeParamPrograms = ndsTypeParamProgramRepository.findByPackageIdAndProgramId(oldPackageId, oldProgram.getProgramId());
        // 3.1.2 Thực hiện clone thông tin đấu nối PCRF nếu có
        if (lstOldNdsTypeParamPrograms.size() > 0) {
            for (NdsTypeParamProgram ntpp : lstOldNdsTypeParamPrograms) {
                cloneNdsTypeParamProgram(ntpp, service, newProgram);
            }
        }
        // 3.4 Clone Transcode
        // Không clone transcode do trùng smsMo
        // 3.5 Clone thông tin bổ sung (Service_info)
        List<ServiceInfo> listServiceInfo = serviceInfoRepository.findAllByPackageId(oldPackageId, oldProgram.getProgramId());
        if (listServiceInfo.size() > 0) {
            for (ServiceInfo serviceInfo: listServiceInfo) {
                cloneServiceInfo(serviceInfo, service, newProgram);
            }
        }
        // RESPONSE
        response.setStatus(ApiResponseStatus.SUCCESS.getValue());
        response.setData(newProgram);
        response.setMessage("Clone thông tin chương trình thành công");
        return response;
    }

    @SneakyThrows(CloneNotSupportedException.class)
    private ServiceProgram cloneServiceProgram(ServiceProgram oldProgram, Long newId) {
        ServiceProgram serviceProgram = (ServiceProgram) oldProgram.clone();
        serviceProgram.setProgramId(null);
        serviceProgram.setServicePackage(servicePackageRepository.findByPackageId(newId));
        System.out.println(oldProgram.getProgramCode());
        if (oldProgram.getProgramCode() != null)
            serviceProgram.setProgramCode(oldProgram.getProgramCode() + "_copy_" + System.currentTimeMillis());
        // a Hải MBF confirm 04/03/2022
        // a Hải MBF confirm 05/03/2022
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
        mapServicePackage.setMapId(null);
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
    private void cloneNdsTypeParamProgram(NdsTypeParamProgram ntpp, ServicePackage newPackage, ServiceProgram newProgram) {
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
        // Tạo Log Action
        LogAction logAction = new LogAction();
        logAction.setTableAction("service_program");
        logAction.setAccount(tokenProvider.account);
        logAction.setAction("DELETE");
        logAction.setOldValue(serviceProgram.toString());
        logAction.setNewValue(null);
        logAction.setTimeAction(new Date());
        logAction.setIdAction(serviceProgram.getProgramId());
        logActionService.add(logAction);
        // Xóa các thành phần liên quan
        // Buckets_info
        List<BucketsInfo> listBucketsInfo = bucketsInfoRepository.findByPackageIdAndProgramId(serviceProgram.getServicePackage().getPackageId(), serviceProgram.getProgramId());
        for (BucketsInfo bucketsInfo : listBucketsInfo) {
            // Tạo Log Action
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
            // Tạo Log Action
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
            // Tạo Log Action
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
            // Tạo Log Action
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
            // Tạo Log Action
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
            // Tạo Log Action
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
        for (MapSmsRespond mapSmsRespond: mapSmsRespondList) {
            // Tạo Log Action
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
}
