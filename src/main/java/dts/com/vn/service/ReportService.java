package dts.com.vn.service;

import dts.com.vn.entities.CommandandSource;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.ilarc.repository.IlArcTaskParameterRepository;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

	private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

	private static final String SOURCE_TYPE = "SOURCE_TYPE";

	private static final String SMS = "SMS";

	private final ServicePackageRepository servicePackageRepository;

	private final ServiceTypeRepository serviceTypeRepository;

	private final RegisterRepository registerRepository;

	private final SasReTaskParameterRepository sasReTaskParameterRepository;

	private final IlArcTaskParameterRepository ilArcTaskParameterRepository;

	@Autowired
	public ReportService(ServicePackageRepository servicePackageRepository, ServiceTypeRepository serviceTypeRepository, RegisterRepository registerRepository, SasReTaskParameterRepository sasReTaskParameterRepository, IlArcTaskParameterRepository ilArcTaskParameterRepository) {
		this.servicePackageRepository = servicePackageRepository;
		this.serviceTypeRepository = serviceTypeRepository;
		this.registerRepository = registerRepository;
		this.sasReTaskParameterRepository = sasReTaskParameterRepository;
		this.ilArcTaskParameterRepository = ilArcTaskParameterRepository;
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
				commandResponse.setSource(getSourceContentIlink(commandResponse.getRequestId()));
				return commandResponse;
			}).collect(Collectors.toList());
		}
		packageResponse.setListCommand(result);
		response.setStatus(200);
		response.setData(packageResponse);
		return response;
	}

	private String getSourceContentIlink(Long requestId) {
		Optional<SourcesResponse> sourcesResponse = sasReTaskParameterRepository.findFlowSourceIlink(requestId, SOURCE_TYPE);
		if (!sourcesResponse.isPresent()) {
			return null;
		}
		switch (sourcesResponse.get().getParametersValue()) {
			case "SMPP":
				return SMS;
			case "MBF":
				Optional<SourcesResponse> sasReTaskParameter = sasReTaskParameterRepository.findFlowSourceIlink(sourcesResponse.get().getRequestId(), "SourceSystem");
				return sasReTaskParameter.get().getParametersValue();
			case "BATCH":
				Optional<SourcesResponse> sasReTaskParameter1 = sasReTaskParameterRepository.findFlowSourceIlink(sourcesResponse.get().getRequestId(), "SO1_SOURCE_CODE");
				return sasReTaskParameter1.get().getParametersValue();
			default:
				return sourcesResponse.get().getParametersValue();
		}
	}


	//Lấy nguồn từ db Ilarc
	private String getSourceContentIlarc(Long requestId) {
		Optional<SourcesResponse> sourcesResponse = ilArcTaskParameterRepository.findFlowSourceIlarc(requestId, SOURCE_TYPE);
		if (!sourcesResponse.isPresent()) {
			return null;
		}
		switch (sourcesResponse.get().getParametersValue()) {
			case "SMPP":
				return SMS;
			case "MBF":
				Optional<SourcesResponse> ilarcReTaskParameter = ilArcTaskParameterRepository.findFlowSourceIlarc(sourcesResponse.get().getRequestId(), "SourceSystem");
				return ilarcReTaskParameter.get().getParametersValue();
			case "BATCH":
				Optional<SourcesResponse> ilarcReTaskParameter1 = ilArcTaskParameterRepository.findFlowSourceIlarc(sourcesResponse.get().getRequestId(), "SO1_SOURCE_CODE");
				return ilarcReTaskParameter1.get().getParametersValue();
			default:
				return sourcesResponse.get().getParametersValue();
		}
	}


	//TinhBdt - xủ lý báo cáo 10 số điện thoại có nhiều bản ghi fail nhất
	public ApiResponse dailyTop10IsdnReport(String date) {
		ApiResponse response = new ApiResponse();
		List<CancelReportResponse> data = new ArrayList<>();
		List<CancelReportResponse> newData = new ArrayList<>();
		if (LongDistance(date).equals("false")) {
			Timestamp startDate = Timestamp.valueOf(date + " " + "00:00:00");
			Timestamp endDate = Timestamp.valueOf(date + " " + "23:59:59");
			List<String> listISDN = ilArcTaskParameterRepository.findIsdnHasFailReq(startDate, endDate);
			if (listISDN.size()!=0) {
				for (String isdn : listISDN) {
					List<CommandandSource> listComandSou = new ArrayList<>();
					CancelReportResponse cancelReportResponse = new CancelReportResponse();
					if (isdn.startsWith("84")) {
						cancelReportResponse.setPhoneNumber(isdn.substring(2));
					} else {
						cancelReportResponse.setPhoneNumber(isdn);
					}
					List<Long> listReqId = ilArcTaskParameterRepository.findReqIdByIsdn(isdn, startDate, endDate);
					for (Long reqId : listReqId) {
						CommandandSource commandandSource = new CommandandSource();
						String source = getSourceContentIlarc(reqId);
						String command = ilArcTaskParameterRepository.findCommandByReqId(reqId);
						commandandSource.setSource(source);
						commandandSource.setCommand(command);
						listComandSou.add(commandandSource);
					}
					Map<String, Long> result1 = listComandSou.stream()
							.collect(Collectors.groupingBy(CommandandSource::getSource, Collectors.counting()));
					result1.entrySet().stream().forEach(r -> cancelReportResponse.setSourceContent(r.getKey()));
					result1.entrySet().stream().forEach(r -> cancelReportResponse.setQuantity(r.getValue()));
					Map<String, Long> result2 = listComandSou.stream().collect(Collectors.groupingBy(CommandandSource::getCommand, Collectors.counting()));
					result2.entrySet().stream().forEach(r -> cancelReportResponse.setCommand(r.getKey()));
					data.add(cancelReportResponse);
				}
				Collections.sort(data, new Comparator<CancelReportResponse>() {
					@Override
					public int compare(CancelReportResponse o1, CancelReportResponse o2) {
						return o2.getQuantity().compareTo(o1.getQuantity());
					}
				});
				for (int i = 0; i < 10; i++) {
					newData.add(data.get(i));
				}
				response.setStatus(200);
				response.setData(newData);
			} else {
				response.setMessage("Không có bản ghi nào cả");
				response.setStatus(200);
				response.setData("");
			}
		}
		if (LongDistance(date).equals("true")) {
			Timestamp startDate = Timestamp.valueOf(date + " " + "00:00:00");
			Timestamp endDate = Timestamp.valueOf(date + " " + "23:59:59");
			List<String> listISDN = sasReTaskParameterRepository.findIsdnHasFailReq(startDate, endDate);
			if (listISDN.size()!=0) {
				for (String isdn : listISDN) {
					List<CommandandSource> listComandSou = new ArrayList<>();
					CancelReportResponse cancelReportResponse = new CancelReportResponse();
					if (isdn.startsWith("84")) {
						cancelReportResponse.setPhoneNumber(isdn.substring(0, 1));
					} else {
						cancelReportResponse.setPhoneNumber(isdn);
					}
					List<Long> listReqId = sasReTaskParameterRepository.findReqIdByIsdn(isdn, startDate, endDate);
					for (Long reqId : listReqId) {
						CommandandSource commandandSource = new CommandandSource();
						String source = getSourceContentIlink(reqId);
						String command = sasReTaskParameterRepository.findCommandByReqId(reqId);
						commandandSource.setSource(source);
						commandandSource.setCommand(command);
						listComandSou.add(commandandSource);
					}
					Map<String, Long> result1 = listComandSou.stream().collect(Collectors.groupingBy(CommandandSource::getSource, Collectors.counting()));
					result1.entrySet().stream().forEach(r -> cancelReportResponse.setQuantity(r.getValue()));
					result1.entrySet().stream().forEach(r -> cancelReportResponse.setSourceContent(r.getKey()));
					Map<String, Long> result2 = listComandSou.stream().collect(Collectors.groupingBy(CommandandSource::getCommand, Collectors.counting()));
					result2.entrySet().stream().forEach(r -> cancelReportResponse.setCommand(r.getKey()));
					data.add(cancelReportResponse);
				}
				Collections.sort(data, new Comparator<CancelReportResponse>() {
					@Override
					public int compare(CancelReportResponse o1, CancelReportResponse o2) {
						return o2.getQuantity().compareTo(o1.getQuantity());
					}
				});
				for (int i = 0; i < 10; i++) {
					newData.add(data.get(i));
				}
				response.setStatus(200);
				response.setData(newData);
			} else {
				response.setMessage("Không có bản ghi nào cả");
				response.setStatus(200);
				response.setData("");
			}
		}
		return response;
	}

	private String LongDistance(String date) {
		String result = new String();
		try {
			String time1 = date + " 00:00:00";
			String time2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date1 = format.parse(time1);
			Date date2 = format.parse(time2);
			long difference = date2.getTime() - date1.getTime();
			long timeDistance = difference / 3600000;
			if (timeDistance < 48) return result = "true";
			else return result = "false";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
