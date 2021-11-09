package dts.com.vn.ilink.service;

import dts.com.vn.response.ApiResponse;

public interface ConditionService {

	ApiResponse findConditionByProgramCodeAndTransaction(String programCode, String transaction);

}
