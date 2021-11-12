package dts.com.vn.ilink.service;

import dts.com.vn.response.ApiResponse;

import java.util.HashMap;
import java.util.List;

public interface ConditionService {

	ApiResponse findAllCondition();

	ApiResponse findConditionByProgramCodeAndTransaction(String programCode, String transaction);

	ApiResponse updateListCondition(String programCode, String transaction, List<HashMap<String, String>> listCondition);

}
