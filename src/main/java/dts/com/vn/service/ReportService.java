package dts.com.vn.service;

import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.repository.RegisterRepository;
import dts.com.vn.repository.ServicePackageRepository;
import dts.com.vn.repository.ServiceTypeRepository;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.DailyReportResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ReportService {

	private final ServicePackageRepository servicePackageRepository;

	private final ServiceTypeRepository serviceTypeRepository;

	private final RegisterRepository registerRepository;

	@Autowired
	public ReportService(ServicePackageRepository servicePackageRepository,
						 ServiceTypeRepository serviceTypeRepository,
						 RegisterRepository registerRepository) {
		this.servicePackageRepository = servicePackageRepository;
		this.serviceTypeRepository = serviceTypeRepository;
		this.registerRepository = registerRepository;
	}

	public ApiResponse dailyReport(Long serviceTypeId, String date) {
		ApiResponse response = new ApiResponse();
		DailyReportResponse data = new DailyReportResponse();
		List<Object> result = new ArrayList<>();
		Optional<ServiceType> optServiceType = serviceTypeRepository.findById(serviceTypeId);
		optServiceType.ifPresent(serviceType -> data.setGroupName(serviceType.getName()));
		List<ServicePackage> listAllPackageSameGroup = servicePackageRepository.findAllByServiceTypeId(serviceTypeId);
		if (listAllPackageSameGroup.size() > 0) {
			for (ServicePackage servicePackage : listAllPackageSameGroup) {
				Timestamp start = Timestamp.valueOf(date + " " + "00:00:00");
				Timestamp end = Timestamp.valueOf(date + " " + "23:59:59");
				Integer numberRecord = registerRepository.findAllByPackageIdAndRegDate(servicePackage.getPackageId(), start, end);
				Map<String, String> object = new HashMap<>();
				object.put("packageCode", servicePackage.getCode());
				object.put("packageName", servicePackage.getName());
				object.put("numberRecord", numberRecord.toString());
				result.add(object);
			}
			data.setListPackage(result);
		}
		response.setStatus(200);
		response.setData(data);
		return response;
	}

}
