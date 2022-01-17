package dts.com.vn.service;

import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.ilink.repository.SasReTaskParameterRepository;
import dts.com.vn.repository.RegisterRepository;
import dts.com.vn.repository.ServicePackageRepository;
import dts.com.vn.repository.ServiceTypeRepository;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.DailyReportResponse;
import dts.com.vn.response.ListPackageResponse;
import dts.com.vn.util.LogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ReportService {

	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	private final ServicePackageRepository servicePackageRepository;

	private final ServiceTypeRepository serviceTypeRepository;

	private final RegisterRepository registerRepository;

	private final SasReTaskParameterRepository sasReTaskParameterRepository;

	@Autowired
	public ReportService(ServicePackageRepository servicePackageRepository,
	                     ServiceTypeRepository serviceTypeRepository,
	                     RegisterRepository registerRepository,
	                     SasReTaskParameterRepository sasReTaskParameterRepository) {
		this.servicePackageRepository = servicePackageRepository;
		this.serviceTypeRepository = serviceTypeRepository;
		this.registerRepository = registerRepository;
		this.sasReTaskParameterRepository = sasReTaskParameterRepository;
	}


	public ApiResponse dailyReport(Long serviceTypeId, String date) {
		ApiResponse response = new ApiResponse();
		DailyReportResponse data = new DailyReportResponse();
		List<CompletableFuture<ListPackageResponse>> result = new ArrayList<>();
		Optional<ServiceType> optServiceType = serviceTypeRepository.findById(serviceTypeId);
		optServiceType.ifPresent(serviceType -> data.setGroupName(serviceType.getName()));
		List<ServicePackage> listAllPackageSameGroup = servicePackageRepository.findAllByServiceTypeId(serviceTypeId);
		Timestamp start = Timestamp.valueOf(date + " " + "00:00:00");
		Timestamp end = Timestamp.valueOf(date + " " + "23:59:59");
		long startExcute = System.currentTimeMillis();
		if (listAllPackageSameGroup.size() > 0) {
			result = listAllPackageSameGroup.stream().map(servicePackage -> CompletableFuture.supplyAsync(() -> {
				ListPackageResponse listPackageResponse = new ListPackageResponse();
				Integer numberRecordSuccess = registerRepository
						.findAllByPackageIdAndRegDate(servicePackage.getPackageId(), start, end);
				Integer numberRecordFailed = sasReTaskParameterRepository
						.findAllFailByParameterValueAndDate(servicePackage.getCode(), start, end);
				listPackageResponse.setPackageCode(servicePackage.getCode());
				listPackageResponse.setPackageName(servicePackage.getName());
				listPackageResponse.setNumberRecordSuccess(numberRecordSuccess);
				listPackageResponse.setNumberRecordFailed(numberRecordFailed);
				return listPackageResponse;
			})).collect(Collectors.toList());
		}
		CompletableFuture.allOf(result.toArray(new CompletableFuture[result.size()])).join();
		data.setListPackage(result.stream().map(future -> {
			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList()));
		long endExcute = System.currentTimeMillis();
		LogUtil.writeLog(logger, "time excute function report", endExcute - startExcute + "ms");
		response.setStatus(200);
		response.setData(data);
		return response;
	}

}
