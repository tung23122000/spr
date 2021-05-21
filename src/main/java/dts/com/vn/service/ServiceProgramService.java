package dts.com.vn.service;

import dts.com.vn.entities.*;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.*;
import dts.com.vn.request.AddServiceProgramRequest;
import dts.com.vn.request.CloneServiceProgramRequest;
import dts.com.vn.response.*;
import dts.com.vn.response.service_package_detail.DetailServicePackageResponse;
import dts.com.vn.response.service_package_detail.DetailServiceProgramResponse;
import dts.com.vn.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.function.Function;

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

	public Page<ServiceProgram> findAll(String search, Pageable pageable) {
		if (StringUtils.hasLength(search))
			return serviceProgramRepository.findAll(search, pageable);
		return serviceProgramRepository.findAll(pageable);
	}

	public ServiceProgram add(AddServiceProgramRequest request) {
		ServicePackage servicePackage =
				servicePackageRepository.findByCode(request.getPackageCode()).get();
		return serviceProgramRepository.save(new ServiceProgram(request, servicePackage));
	}

	public ServiceProgram findById(Long id) {
		return serviceProgramRepository.findById(id).get();
	}

	public ServiceProgram update(AddServiceProgramRequest request) {
		ServiceProgram servicePr = findById(request.getServiceProgramId());
		if (Objects.nonNull(servicePr)) {
			ServicePackage servicePackage =
					servicePackageRepository.findByCode(request.getPackageCode()).get();
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
			servicePr.setExtendEndDate(
					DateTimeUtil.convertStringToInstant(request.getExtendEndDate(), "dd/MM/yyyy HH:mm:ss"));
			servicePr.setEndDate(
					DateTimeUtil.convertStringToInstant(request.getExtendEndDate(), "dd/MM/yyyy HH:mm:ss"));
			servicePr.setDescription(request.getDescription());
			servicePr.setMinStepMinus(request.getMinStepMinus());
			servicePr.setCheckStepType(request.getCheckStepType());
			servicePr.setCommandAlias(request.getCommandAlias());
			return serviceProgramRepository.save(servicePr);
		}
		throw new RestApiException(ErrorCode.UPDATE_FAILURE);
	}

	public DetailServicePackageResponse findByPackageId(Long packageId, Pageable pageable) {
		ServicePackage entity = servicePackageRepository.findById(packageId)
				.orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));
		ServicePackageResponse response = new ServicePackageResponse(entity);
		Page<ServiceProgram> listPage = serviceProgramRepository.findByPackageId(packageId, pageable);
		Page<ServiceProgramResponse> pageResponse =
				listPage.map(new Function<ServiceProgram, ServiceProgramResponse>() {
					@Override
					public ServiceProgramResponse apply(ServiceProgram service) {
						return new ServiceProgramResponse(service);
					}
				});
		return new DetailServicePackageResponse(response, pageResponse);
	}

	public DetailServiceProgramResponse detailServiceProgram(Long programId, Pageable pageableIN,
	                                                         Pageable pageableBILLING, Pageable pageablePCRF, Pageable pageServiceInfo) {
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
		Page<ServiceInfo> pageService =
				serviceInfoRepository.findAllByProgramId(programId, pageServiceInfo);
		Page<ServiceInfoResponse> pageServiceRes =
				pageService.map(new Function<ServiceInfo, ServiceInfoResponse>() {
					@Override
					public ServiceInfoResponse apply(ServiceInfo t) {
						return new ServiceInfoResponse(t);
					}
				});
		return new DetailServiceProgramResponse(serviceProgramResponse, pageInRes, pageBillingRes,
				pagePCRFRes, pageServiceRes);
	}
}
