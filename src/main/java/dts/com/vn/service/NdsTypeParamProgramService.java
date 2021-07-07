package dts.com.vn.service;

import dts.com.vn.entities.NdsTypeParamProgram;
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.NdsTypeParamProgramRepository;
import dts.com.vn.repository.ServiceProgramRepository;
import dts.com.vn.request.NdsTypeParamProgramRequest;
import dts.com.vn.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class NdsTypeParamProgramService {

	@Autowired
	private NdsTypeParamProgramRepository ndsTypeParamProgramRepository;

	@Autowired
	private ServiceProgramRepository serviceProgramRepository;

	public Page<NdsTypeParamProgram> findAllByProgramId(Long programId, Pageable pageable) {
		return ndsTypeParamProgramRepository.findAllByProgramId(programId, pageable);
	}

	public NdsTypeParamProgram add(NdsTypeParamProgramRequest request) {
		ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getServiceProgramId())
				.orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
		return ndsTypeParamProgramRepository.save(new NdsTypeParamProgram(request, serviceProgram));
	}

	public NdsTypeParamProgram findById(Long ndsId) {
		return ndsTypeParamProgramRepository.findById(ndsId)
				.orElseThrow(() -> new RestApiException(ErrorCode.NDS_TYPE_PARAM_PROGRAM_NOT_FOUND));
	}

	public NdsTypeParamProgram update(NdsTypeParamProgramRequest request) {
		NdsTypeParamProgram entity = findById(request.getNdsTypeParamKey());
		ServiceProgram serviceProgram = serviceProgramRepository.findById(request.getServiceProgramId())
				.orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
		entity.setServiceProgram(serviceProgram);
		entity.setServicePackage(serviceProgram.getServicePackage());
		entity.setNdsType(request.getNdsType());
		entity.setNdsParam(request.getNdsParam());
		entity.setNdsValue(request.getNdsValue());
		entity.setStaDatetime(
				DateTimeUtil.convertStringToInstant(request.getStaDatetime(), "dd/MM/yyyy HH:mm:ss"));
		entity.setEndDatetime(
				DateTimeUtil.convertStringToInstant(request.getEndDatetime(), "dd/MM/yyyy HH:mm:ss"));
		return ndsTypeParamProgramRepository.save(entity);
	}

	public Page<NdsTypeParamProgram> findAll(String search, Pageable pageable) {
		if (StringUtils.hasLength(search)) {
			return ndsTypeParamProgramRepository.findAll(search, pageable);
		}
		return ndsTypeParamProgramRepository.findAll(pageable);
	}
}
