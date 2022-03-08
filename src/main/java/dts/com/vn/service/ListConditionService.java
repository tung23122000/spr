package dts.com.vn.service;


import dts.com.vn.request.NewListConditionRequest;
import dts.com.vn.response.ApiResponse;


public interface ListConditionService {

    ApiResponse addListCondition(NewListConditionRequest newListConditionRequest);

    ApiResponse updateIsPackage(Integer id, Boolean isPackage);

    ApiResponse deleteListCondition(Integer id);
}
