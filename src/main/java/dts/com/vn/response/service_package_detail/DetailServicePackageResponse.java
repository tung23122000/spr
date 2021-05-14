package dts.com.vn.response.service_package_detail;

import dts.com.vn.response.ServicePackageResponse;
import dts.com.vn.response.ServiceProgramResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class DetailServicePackageResponse {

	private ServicePackageResponse servicePackageResponse;

	private Page<ServiceProgramResponse> listServiceProgram;

	public DetailServicePackageResponse(ServicePackageResponse servicePackageResponse,
	                                    Page<ServiceProgramResponse> listServiceProgram) {
		this.servicePackageResponse = servicePackageResponse;
		this.listServiceProgram = listServiceProgram;
	}
}
