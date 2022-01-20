package dts.com.vn.service;

import dts.com.vn.entities.ConfigFlowCondition;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.repository.ConfigFlowConditionRepository;
import dts.com.vn.request.ConfigFlowConditionRequest;
import dts.com.vn.response.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author BinhDT
 * @version 1
 * @apiNote CRUD for config flow condition
 * @created 19/01/2022
 */
@Service
public class ConfigFlowConditionService {

	@Autowired
	private ConfigFlowConditionRepository configFlowConditionRepository;


	public ApiResponse findAllConditon() {
		List<ConfigFlowCondition> flowConditions = configFlowConditionRepository.findAll()
				.stream()
				.sorted(Comparator.comparing(ConfigFlowCondition::getConditionId))
				.filter(configFlowCondition -> StringUtils.equals(configFlowCondition.getIsConfig().toString(), "1"))
				.collect(Collectors.toList());
		ApiResponse response = new ApiResponse();
		response.setStatus(200);
		response.setData(flowConditions);
		response.setMessage("Lấy danh sách luồng điều kiện");
		return response;
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
		flowCondition.setIsConfig(configFlowCondition.getIsConfig());
		ConfigFlowCondition response = configFlowConditionRepository.save(flowCondition);

		return new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), response, null, "Tạo mới đối tượng thành công!");
	}

	public ApiResponse delete(Long id) {
		Optional<ConfigFlowCondition> condition = configFlowConditionRepository.findById(id);
		if (condition.isPresent()) {
			configFlowConditionRepository.delete(condition.get());
			new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), condition.get(), null, "Xóa đối tượng thành công!");
		}
		return new ApiResponse(ApiResponseStatus.FAILED.getValue(), condition, null, "Xóa đối tượng không thành công!");
	}

	public ApiResponse update(ConfigFlowConditionRequest configFlowCondition) {

		ConfigFlowCondition condition = new ConfigFlowCondition();
		condition.setConditionId(configFlowCondition.getRequestId());
		condition.setFlowName(configFlowCondition.getFlowName());
		condition.setIsConfig(configFlowCondition.getIsConfig());
		ConfigFlowCondition flowCondition = configFlowConditionRepository.saveAndFlush(condition);
		return new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), flowCondition, null, "Update điều kiện thành công!");
	}

}
