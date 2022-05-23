package dts.com.vn.service;

import dts.com.vn.request.InfoTargetSystemRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.InfoTargetSystemResponse;

public interface InfoTargetSystemService {

    ApiResponse findAllInfo();

    ApiResponse createInfoTargetSystem(InfoTargetSystemRequest request);

    ApiResponse getDetail(Long id);

    ApiResponse updateInfo(InfoTargetSystemResponse request);

    ApiResponse deleteInfo(Long id);

    ApiResponse getAllSystemsNames();
}
