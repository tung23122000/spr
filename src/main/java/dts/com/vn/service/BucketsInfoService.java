package dts.com.vn.service;

import dts.com.vn.entities.BucketsInfo;
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.BucketsInfoRepository;
import dts.com.vn.repository.ServicePackageRepository;
import dts.com.vn.repository.ServiceProgramRepository;
import dts.com.vn.request.AddBucketsInfoRequest;
import dts.com.vn.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class BucketsInfoService {

	@Autowired
	private BucketsInfoRepository bucketsInfoRepository;

	@Autowired
	private ServicePackageRepository servicePackageRepository;

	@Autowired
	private ServiceProgramRepository serviceProgramRepository;

	public Page<BucketsInfo> findAll(String search, Pageable pageable) {
		if (StringUtils.hasLength(search)) {
			return bucketsInfoRepository.findAll(search, pageable);
		}
		return bucketsInfoRepository.findAll(pageable);
	}

	public BucketsInfo add(AddBucketsInfoRequest request) {
		if (request.getBundleType() == null)
			request.setBundleType("0");
		ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getProgramId())
				.orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
		return bucketsInfoRepository.save(new BucketsInfo(request, serviceProgram));
	}

	public BucketsInfo update(AddBucketsInfoRequest request) {
		BucketsInfo bucketsInfo = findById(request.getBucketsId());
		servicePackageRepository.findById(request.getPackageId())
				.orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));
		serviceProgramRepository.findById(request.getProgramId())
				.orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
		bucketsInfo.setBucName(request.getBucName());
		bucketsInfo.setBucType(request.getBucType());
		bucketsInfo.setBucAddUnit(request.getBucAddUnit());
		bucketsInfo.setBucAddDay(request.getBucAddDay());
		bucketsInfo.setBundleType(request.getBundleType());
		bucketsInfo.setSubsType(request.getSubsType());
		bucketsInfo.setSubsDelayTime(request.getSubDelayTime());
		bucketsInfo.setBucUnit(request.getBucUnit());
		bucketsInfo.setStaDate(
				DateTimeUtil.convertStringToInstant(request.getStartDate(), "dd/MM/yyyy HH:mm:ss"));
		bucketsInfo.setEndDate(
				DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss"));
		bucketsInfo.setMobType(request.getMobType());
		bucketsInfo.setServiceIn(request.getServiceIn());
		return bucketsInfoRepository.save(bucketsInfo);
	}

	public BucketsInfo findById(Long id) {
		return bucketsInfoRepository.findById(id)
				.orElseThrow(() -> new RestApiException(ErrorCode.API_FAILED_UNKNOWN));
	}
}
