package dts.com.vn.service.serviceImpl;

import dts.com.vn.entities.ConfigFlowCondition;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.repository.ConfigFlowConditionRepository;
import dts.com.vn.request.ConfigFlowConditionRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ConfigFlowConditionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConfigFlowConditionServiceImpl implements ConfigFlowConditionService {

	private final ConfigFlowConditionRepository configFlowConditionRepository;

	@Autowired
	public ConfigFlowConditionServiceImpl(ConfigFlowConditionRepository configFlowConditionRepository) {
		this.configFlowConditionRepository = configFlowConditionRepository;
	}

	@Transactional
	public ApiResponse save(ConfigFlowConditionRequest configFlowCondition) {
		List<ConfigFlowCondition> flowConditions = configFlowConditionRepository.findAll();
		for (ConfigFlowCondition flowCondition : flowConditions) {
			if (StringUtils.equalsIgnoreCase(flowCondition.getFlowName(), configFlowCondition.getFlowName())) {
				return new ApiResponse(ApiResponseStatus.FAILED.getValue(), flowCondition, null, "Đối tượng đã tồn tại!");
			}
		}
		ConfigFlowCondition flowCondition = new ConfigFlowCondition();
		flowCondition.setFlowName(configFlowCondition.getFlowName());
		flowCondition.setConfig(configFlowCondition.isConfig());
		ConfigFlowCondition response = configFlowConditionRepository.save(flowCondition);
		return new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), response, null, "Tạo mới đối tượng thành công!");
	}

	public ApiResponse findByConditonIsTrue() {
		List<ConfigFlowCondition> flowConditions = configFlowConditionRepository
				.findAll()
				.stream()
				.sorted(Comparator.comparing(ConfigFlowCondition::getConditionId))
				.filter(ConfigFlowCondition::isConfig)
				.collect(Collectors.toList());
		ApiResponse response = new ApiResponse();
		response.setStatus(200);
		response.setData(flowConditions);
		response.setMessage("Lấy danh sách luồng điều kiện với status là 1");
		return response;
	}

	public ApiResponse findAllConditon() {
		List<ConfigFlowCondition> flowConditions = configFlowConditionRepository
				.findAll()
				.stream()
				.sorted(Comparator.comparing(ConfigFlowCondition::getConditionId))
				.collect(Collectors.toList());
		ApiResponse response = new ApiResponse();
		response.setStatus(200);
		response.setData(flowConditions);
		response.setMessage("Lấy danh sách luồng điều kiện với tất cả các điều kiện!!");
		return response;
	}


	public ApiResponse update(List<ConfigFlowConditionRequest> configFlowConditionRequests) {
		List<ConfigFlowCondition> flowConditions = new ArrayList<>();
		configFlowConditionRequests.forEach(configFlowConditionRequest -> {
			Optional<ConfigFlowCondition> flowConditionOptional = configFlowConditionRepository.findById(configFlowConditionRequest.getConditionId());
			if (!flowConditionOptional.isPresent()) {
				throw new RuntimeException("Không tồn tại bản ghi trong bảng: ConfigFlowCondition");
			}
			ConfigFlowCondition condition = new ConfigFlowCondition();
			condition.setConditionId(configFlowConditionRequest.getConditionId());
			condition.setFlowKey(configFlowConditionRequest.getFlowKey());
			condition.setFlowName(configFlowConditionRequest.getFlowName());
			condition.setConfig(configFlowConditionRequest.isConfig());
			ConfigFlowCondition flowCondition = configFlowConditionRepository.saveAndFlush(condition);
			flowConditions.add(flowCondition);

		});
		return new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), flowConditions.size(), null, "Update điều kiện thành công!");
	}

	public ApiResponse delete(Long id) {
		Optional<ConfigFlowCondition> condition = configFlowConditionRepository.findById(id);
		if (condition.isPresent()) {
			configFlowConditionRepository.delete(condition.get());
			return new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), condition.get(), null, "Xóa đối tượng thành công!");
		}
		return new ApiResponse(ApiResponseStatus.FAILED.getValue(), condition, null, "Xóa đối tượng không thành công!");
	}
}
