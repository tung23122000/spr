package dts.com.vn.ilink.service;

import dts.com.vn.ilink.entities.Condition;
import dts.com.vn.response.ApiResponse;

import java.util.List;

public interface ConditionService {

	ApiResponse findAllCondition();

	ApiResponse findConditionByProgramCodeAndTransaction(String programCode, String transaction, Long packageId);

	ApiResponse createListCondition(String programCode, String transaction, List<Condition> listCondition, Long packageId);

	ApiResponse updateListCondition(String programCode, String transaction, List<Condition> listCondition,
									Long packageId);

	ApiResponse deleteListCondition(String programCode, String transaction);

}
