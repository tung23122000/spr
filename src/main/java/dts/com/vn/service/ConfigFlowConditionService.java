package dts.com.vn.service;


import dts.com.vn.request.ConfigFlowConditionRequest;
import dts.com.vn.response.ApiResponse;

public interface ConfigFlowConditionService {

	/**
	 * * Description Create new Config flow condition
	 *
	 * @param configFlowCondition - Object configFlowCondition request
	 * @return apiResponse
	 * @author BinhDT
	 * @created 19/01/2022
	 */
	ApiResponse save(ConfigFlowConditionRequest configFlowCondition);

	/**
	 * * Description find all flow condition
	 *
	 * @return apiResponse
	 * @author BinhDT
	 * @created 19/01/2022
	 */
	ApiResponse findAllConditon();

	/**
	 * * Description update flow condition
	 *
	 * @param configFlowCondition - Object configFlowCondition by request
	 * @return apiResponse
	 * @author BinhDT
	 * @created 19/01/2022
	 */
	ApiResponse update(ConfigFlowConditionRequest configFlowCondition);

	/**
	 * * Description delete condition by id
	 *
	 * @param id - Id of flow condition
	 * @return apiResponse
	 * @author BinhDT
	 * @created 19/01/2022
	 */
	ApiResponse delete(Long id);
}
