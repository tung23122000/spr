package dts.com.vn.service;

import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.ilink.repository.SasReTaskParameterRepository;
import dts.com.vn.repository.RegisterRepository;
import dts.com.vn.repository.ServicePackageRepository;
import dts.com.vn.repository.ServiceTypeRepository;
import dts.com.vn.response.*;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.DailyReportResponse;
import dts.com.vn.response.ListPackageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	private final ServicePackageRepository servicePackageRepository;

	private final ServiceTypeRepository serviceTypeRepository;

	private final RegisterRepository registerRepository;

	private final SasReTaskParameterRepository sasReTaskParameterRepository;

	@Autowired
	public ReportService(ServicePackageRepository servicePackageRepository, ServiceTypeRepository serviceTypeRepository, RegisterRepository registerRepository, SasReTaskParameterRepository sasReTaskParameterRepository) {
		this.servicePackageRepository = servicePackageRepository;
		this.serviceTypeRepository = serviceTypeRepository;
		this.registerRepository = registerRepository;
		this.sasReTaskParameterRepository = sasReTaskParameterRepository;
	}


	public ApiResponse dailyReport(Long serviceTypeId, String date) {
		Long phones = registerRepository.findAllPhone();

		ApiResponse response = new ApiResponse();
		DailyReportResponse data = new DailyReportResponse();
		data.setPhoneNumber(phones);
		List<CompletableFuture<ListPackageResponse>> result = new ArrayList<>();
		Optional<ServiceType> optServiceType = serviceTypeRepository.findById(serviceTypeId);
		optServiceType.ifPresent(serviceType -> data.setGroupName(serviceType.getName()));
		List<ServicePackage> listAllPackageSameGroup = servicePackageRepository.findAllByServiceTypeId(serviceTypeId);
		Timestamp start = Timestamp.valueOf(date + " " + "00:00:00");
		Timestamp end = Timestamp.valueOf(date + " " + "23:59:59");
		if (listAllPackageSameGroup.size() > 0) {
			result = listAllPackageSameGroup.stream().map(servicePackage -> CompletableFuture.supplyAsync(() -> {
				ListPackageResponse listPackageResponse = new ListPackageResponse();
				Integer numberRecordSuccess = registerRepository.findAllByPackageIdAndRegDate(servicePackage.getPackageId(), start, end);
				Integer numberRecordFailed = sasReTaskParameterRepository.findAllFailByParameterValueAndDate(servicePackage.getCode(), start, end);

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
		response.setStatus(200);
		response.setData(data);
		return response;
	}

	/**
	 * Description - Hàm xử lý lấy thông tin từ package và transaction
	 *
	 * @author - binhDT
	 * @created - 17/01/2022
	 */
	public ApiResponse findByPackageAndTransaction(Long serviceTypeId, String packageCode, String transaction, String date) {

		ApiResponse response = new ApiResponse();
		ReportServicePackageResponse packageResponse = new ReportServicePackageResponse();
		List<ListCommandResponse> result = new ArrayList<>();
		Optional<ServiceType> optServiceType = serviceTypeRepository.findById(serviceTypeId);
		optServiceType.ifPresent(serviceType -> packageResponse.setServiceGroup(serviceType.getName()));
		// Lấy thông tin servicePackage thông qua serviceTypeId và packageCode
		ServicePackage servicePackage = servicePackageRepository.findByServiceTypeIdAndCode(serviceTypeId, packageCode);
		Timestamp start = Timestamp.valueOf(date + " " + "00:00:00");
		Timestamp end = Timestamp.valueOf(date + " " + "23:59:59");
		// Số lượng request thành công
		Integer numberRecordSuccess = registerRepository.findAllByPackageIdAndRegDate(servicePackage.getPackageId(), start, end);
		// Số lượng request thất bại
		Integer numberRecordFailed = sasReTaskParameterRepository.findAllFailByParameterValueAndDate(servicePackage.getCode(), start, end);

		//Set giá trị vào response
		packageResponse.setPackageCode(servicePackage.getCode());
		packageResponse.setPackageName(servicePackage.getName());
		packageResponse.setNumberRecordSuccess(numberRecordSuccess);
		packageResponse.setNumberRecordFailed(numberRecordFailed);

		// Lấy ra danh sách thông tin request sms_content và trạng thái
		List<CommandResponse> commandResponseList = sasReTaskParameterRepository.findByListCommand(packageCode, transaction, start, end);

		// Kiểm tra danh sách tin nhắn và map vào list response
		if (commandResponseList.size() > 0) {
			result = commandResponseList.stream().map(command -> {
				ListCommandResponse commandResponse = new ListCommandResponse();
				commandResponse.setRequestId(command.getRequestId());
				commandResponse.setRequestCommand(command.getRequestCommand());
				commandResponse.setRequestStatus(command.getRequestStatus());
				return commandResponse;
			}).collect(Collectors.toList());
		}
		packageResponse.setListCommand(result);
		response.setStatus(200);
		response.setData(packageResponse);
		return response;
	}

}
