package dts.com.vn.response.service_package_detail;

import dts.com.vn.response.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

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

	// TRANSACTION
	private Page<MapCommandAliasResponse> pageTransaction;

	private Page<ServiceInfoResponse> pageServiceInfoResponse;

	public DetailServiceProgramResponse(ServiceProgramResponse serviceProgramResponse,
	                                    Page<BucketsInfoResponse> pageBucketsInfo,
	                                    Page<MapServicePackageResponse> pageMapServicePackage,
	                                    Page<NdsTypeParamProgramResponse> pageNdsTypeParamProgram,
										Page<MapCommandAliasResponse> pageTransaction,
	                                    Page<ServiceInfoResponse> pageServiceInfoResponse
	) {
		this.serviceProgramResponse = serviceProgramResponse;
		this.pageBucketsInfo = pageBucketsInfo;
		this.pageMapServicePackage = pageMapServicePackage;
		this.pageNdsTypeParamProgram = pageNdsTypeParamProgram;
		this.pageTransaction = pageTransaction;
		this.pageServiceInfoResponse = pageServiceInfoResponse;
	}
}
