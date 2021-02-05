package dts.com.vn.response.service_package_detail;

import lombok.Setter;
import org.springframework.data.domain.Page;
import dts.com.vn.response.BucketsInfoResponse;
import dts.com.vn.response.MapServicePackageResponse;
import dts.com.vn.response.NdsTypeParamProgramResponse;
import dts.com.vn.response.ServiceInfoResponse;
import dts.com.vn.response.ServiceProgramResponse;
import lombok.Getter;

@Getter
@Setter
public class DetailServiceProgramResponse {

  private ServiceProgramResponse serviceProgramResponse;
  
  // IN
  private Page<BucketsInfoResponse> pageBucketsInfo;
  
  // BILLING
  private Page<MapServicePackageResponse> pageMapServicePackage;
  
  // PCRF
  private Page<NdsTypeParamProgramResponse> pageNdsTypeParamProgram;
  
  private Page<ServiceInfoResponse> pageServiceInfoResponse;

  public DetailServiceProgramResponse(ServiceProgramResponse serviceProgramResponse,
      Page<BucketsInfoResponse> pageBucketsInfo,
      Page<MapServicePackageResponse> pageMapServicePackage,
      Page<NdsTypeParamProgramResponse> pageNdsTypeParamProgram,
      Page<ServiceInfoResponse> pageServiceInfoResponse
      ) {
    this.serviceProgramResponse = serviceProgramResponse;
    this.pageBucketsInfo = pageBucketsInfo;
    this.pageMapServicePackage = pageMapServicePackage;
    this.pageNdsTypeParamProgram = pageNdsTypeParamProgram;
    this.pageServiceInfoResponse = pageServiceInfoResponse;
  }
}
