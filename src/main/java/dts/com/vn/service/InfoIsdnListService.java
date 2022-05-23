package dts.com.vn.service;

import dts.com.vn.request.InfoIsdnListRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.InfoIsdnListResponse;

public interface InfoIsdnListService {

    ApiResponse findAllInfo();

    ApiResponse createInfo(InfoIsdnListRequest request);

    ApiResponse getDetailInfo(Long id);

    ApiResponse updateInfo(InfoIsdnListResponse request);

    ApiResponse deleteInfo(Long id);

    ApiResponse getListIsdnListId();

}
