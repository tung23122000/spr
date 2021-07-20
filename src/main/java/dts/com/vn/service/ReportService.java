package dts.com.vn.service;

import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.repository.RegisterRepository;
import dts.com.vn.repository.ServicePackageRepository;
import dts.com.vn.repository.ServiceTypeRepository;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.DailyReportResponse;
import dts.com.vn.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
		// Format date
		String dateFormat = DateTimeUtil.formatDate(date);
		Optional<ServiceType> optServiceType = serviceTypeRepository.findById(serviceTypeId);
		optServiceType.ifPresent(serviceType -> data.setGroupName(serviceType.getName()));
		List<ServicePackage> listAllPackageSameGroup = servicePackageRepository.findAllByServiceTypeId(serviceTypeId);
		if (listAllPackageSameGroup.size() > 0) {
			for (ServicePackage servicePackage : listAllPackageSameGroup) {
				Instant start = DateTimeUtil.convertStringToInstant(dateFormat + " " + "00:00:00", DateTimeUtil.DD_MM_YYYY_HH_mm_ss);
				Instant end = DateTimeUtil.convertStringToInstant(dateFormat + " " + "23:59:59", DateTimeUtil.DD_MM_YYYY_HH_mm_ss);
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
