package dts.com.vn.service;

import dts.com.vn.entities.CommandandSource;
import dts.com.vn.entities.Register;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.ilarc.repository.IlArcTaskParameterRepository;
import dts.com.vn.ilink.repository.SasReTaskParameterRepository;
import dts.com.vn.repository.RegisterRepository;
import dts.com.vn.repository.ServicePackageRepository;
import dts.com.vn.repository.ServiceTypeRepository;
import dts.com.vn.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReportService {

    private static final String SOURCE_TYPE = "SOURCE_TYPE";

    private static final String SMS = "SMS";

    private final ServicePackageRepository servicePackageRepository;

    private final ServiceTypeRepository serviceTypeRepository;

    private final RegisterRepository registerRepository;

    private final SasReTaskParameterRepository sasReTaskParameterRepository;

    private final IlArcTaskParameterRepository ilArcTaskParameterRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ReportService(ServicePackageRepository servicePackageRepository,
                         ServiceTypeRepository serviceTypeRepository,
                         RegisterRepository registerRepository,
                         SasReTaskParameterRepository sasReTaskParameterRepository,
                         IlArcTaskParameterRepository ilArcTaskParameterRepository) {
        this.servicePackageRepository = servicePackageRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.registerRepository = registerRepository;
        this.sasReTaskParameterRepository = sasReTaskParameterRepository;
        this.ilArcTaskParameterRepository = ilArcTaskParameterRepository;
    }

    /**
     * Description - Tìm tổng số thuê bao đang có gói còn hiệu lực
     * Sử dụng đa luồng để tính toán nhanh
     *
     * @return Number - Tổng số thuê bao đang có gói còn hiệu lực trên hệ thống
     * @author - giangdh
     * @created - 07/04/2022
     */
    public Long findAllPhoneNumberHaveActivePackage() {
        Register reg = new Register();
        List<CompletableFuture<Long>> listCount = new ArrayList<>();
        CompletableFuture<Long> future;
        for (int i = 1; i < 10; i++) {
            int numberOfPartition = i;
            future = CompletableFuture.supplyAsync(() -> reg.getPhoneNumber(entityManager, "\"" + "PART" + numberOfPartition + "\""));
            listCount.add(future);
        }
        List<Long> listResponse = listCount.stream()
                                      .map(CompletableFuture::join)
                                      .collect(Collectors.toList());
        long amout = 0L;
        for (Long aLong : listResponse) {
            amout += aLong;
        }
        return amout;

    }


    /**
     * Description -
     *
     * @param serviceTypeId -
     * @return any
     * @author - giangdh
     * @created - 07/04/2022
     */
    public ApiResponse dailyReport(Long serviceTypeId, String date) throws InterruptedException {
        ApiResponse response = new ApiResponse();
        DailyReportResponse data = new DailyReportResponse();
        List<CompletableFuture<ListPackageResponse>> result = new ArrayList<>();
        Long timeDifference = isTwoDay(date);
        Optional<ServiceType> optServiceType = serviceTypeRepository.findById(serviceTypeId);
        optServiceType.ifPresent(serviceType -> data.setGroupName(serviceType.getName()));
        List<ServicePackage> listAllPackageSameGroup = servicePackageRepository.findAllByServiceTypeId(serviceTypeId);
        Timestamp start = Timestamp.valueOf(date + " " + "00:00:00");
        Timestamp end = Timestamp.valueOf(date + " " + "23:59:59");
        int requestSize = listAllPackageSameGroup.size();
        if (requestSize > 0) {
            ExecutorService executorService = Executors.newFixedThreadPool(requestSize);
            List<Callable<ListPackageResponse>> callables = new ArrayList<>();
            listAllPackageSameGroup.forEach(servicePackage -> {
                ListPackageResponse listPackageResponse = new ListPackageResponse();
                callables.add(() -> {
                    Integer numberRecordSuccess;
                    Integer numberRecordFailed;
                    if (timeDifference > 0) {
                        numberRecordSuccess = ilArcTaskParameterRepository.findAllSuccessByParameterValueInIlarc(servicePackage.getCode(), start, end);
                        numberRecordFailed = ilArcTaskParameterRepository.findAllFailByParameterValueInIlarc(servicePackage.getCode(), start, end);
                    } else if (timeDifference < 0) {
                        numberRecordSuccess = sasReTaskParameterRepository.findAllSuccessByParameterValueInIlink(servicePackage.getCode(), start, end);
                        numberRecordFailed = sasReTaskParameterRepository.findAllFailByParameterValueInIlink(servicePackage.getCode(), start, end);
                    } else {
                        Integer numberRecordSuccessIlarc = ilArcTaskParameterRepository.findAllSuccessByParameterValueInIlarc(servicePackage.getCode(), start, end);
                        Integer numberRecordFailedIlarc = ilArcTaskParameterRepository.findAllFailByParameterValueInIlarc(servicePackage.getCode(), start, end);
                        Integer numberRecordSuccessIlink = sasReTaskParameterRepository.findAllSuccessByParameterValueInIlink(servicePackage.getCode(), Timestamp.valueOf(date + " " + "23:30:00"), Timestamp.valueOf(date + " " + "23:59:59"));
                        Integer numberRecordFailedIlink = sasReTaskParameterRepository.findAllFailByParameterValueInIlink(servicePackage.getCode(), Timestamp.valueOf(date + " " + "23:30:00"), Timestamp.valueOf(date + " " + "23:59:59"));
                        numberRecordFailed = numberRecordFailedIlarc + numberRecordFailedIlink;
                        numberRecordSuccess = numberRecordSuccessIlink + numberRecordSuccessIlarc;
                    }
                    listPackageResponse.setPackageCode(servicePackage.getCode());
                    listPackageResponse.setPackageName(servicePackage.getName());
                    listPackageResponse.setNumberRecordSuccess(numberRecordSuccess);
                    listPackageResponse.setNumberRecordFailed(numberRecordFailed);
                    return listPackageResponse;
                });
            });
            List<Future<ListPackageResponse>> futures = executorService.invokeAll(callables);
            data.setListPackage(futures.stream().map(listPackageResponseFuture -> {
                try {
                    return listPackageResponseFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList()));
        }
        response.setStatus(200);
        response.setData(data);
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
                Optional<SourcesResponse> sasReTaskParameter = sasReTaskParameterRepository.findFlowSourceIlink(sourcesResponse.get()
                                                                                                                               .getRequestId(), "SourceSystem");
                if (sasReTaskParameter.isPresent()) {
                    return sasReTaskParameter.get().getParametersValue();
                }
            case "BATCH":
                Optional<SourcesResponse> sasReTaskParameter1 = sasReTaskParameterRepository.findFlowSourceIlink(sourcesResponse.get()
                                                                                                                                .getRequestId(), "SO1_SOURCE_CODE");
                if (sasReTaskParameter1.isPresent()) {
                    return sasReTaskParameter1.get().getParametersValue();
                }
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
                Optional<SourcesResponse> ilarcReTaskParameter = ilArcTaskParameterRepository.findFlowSourceIlarc(sourcesResponse.get()
                                                                                                                                 .getRequestId(), "SourceSystem");
                if (ilarcReTaskParameter.isPresent()) {
                    return ilarcReTaskParameter.get().getParametersValue();
                }
            case "BATCH":
                Optional<SourcesResponse> ilarcReTaskParameter1 = ilArcTaskParameterRepository.findFlowSourceIlarc(sourcesResponse.get()
                                                                                                                                  .getRequestId(), "SO1_SOURCE_CODE");
                if (ilarcReTaskParameter1.isPresent()) {
                    return ilarcReTaskParameter1.get().getParametersValue();
                }
            default:
                return sourcesResponse.get().getParametersValue();
        }
    }


    /**
     * Description - Xử lý báo cáo 10 số điện thoại có nhiều bản ghi fail nhất
     *
     * @param date - Ngày cần truy xuất dữ liệu
     * @return any
     * @author - tinhbdt
     * @created - 09/02/2022
     */
    public ApiResponse dailyTop10IsdnReport(String date) {
        ApiResponse response = new ApiResponse();
        List<CancelReportResponse> data = new ArrayList<>();
        List<CancelReportResponse> newData = new ArrayList<>();
        Timestamp startDate = Timestamp.valueOf(date + " " + "00:00:00");
        Timestamp endDate = Timestamp.valueOf(date + " " + "23:59:59");
        Long timeDifference = isTwoDay(date);
        if (timeDifference > 0) {
            List<String> listISDN = ilArcTaskParameterRepository.findIsdnHasFailReq(startDate, endDate);
            if (listISDN.size() != 0) {
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
                    result1.forEach((key2, value2) -> cancelReportResponse.setSourceContent(key2));
                    result1.forEach((key1, value1) -> cancelReportResponse.setQuantity(value1));
                    Map<String, Long> result2 = listComandSou.stream()
                                                             .collect(Collectors.groupingBy(CommandandSource::getCommand, Collectors.counting()));
                    result2.forEach((key, value) -> cancelReportResponse.setCommand(key));
                    data.add(cancelReportResponse);
                }
                data.sort((o1, o2) -> o2.getQuantity().compareTo(o1.getQuantity()));
                for (int i = 0; i < data.size(); i++) {
                    if (i < 10) {
                        newData.add(data.get(i));
                    }
                }
                response.setStatus(200);
                response.setData(newData);
            } else {
                response.setMessage("Không có bản ghi nào cả");
                response.setStatus(200);
                response.setData(newData);
            }
        } else {
            List<String> listISDN = sasReTaskParameterRepository.findIsdnHasFailReq(startDate, endDate);
            if (listISDN.size() != 0) {
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
                    Map<String, Long> result1 = listComandSou.stream()
                                                             .collect(Collectors.groupingBy(CommandandSource::getSource, Collectors.counting()));
                    result1.forEach((key1, value1) -> cancelReportResponse.setQuantity(value1));
                    result1.forEach((key2, value2) -> cancelReportResponse.setSourceContent(key2));
                    Map<String, Long> result2 = listComandSou.stream()
                                                             .collect(Collectors.groupingBy(CommandandSource::getCommand, Collectors.counting()));
                    result2.forEach((key, value) -> cancelReportResponse.setCommand(key));
                    data.add(cancelReportResponse);
                }
                data.sort((o1, o2) -> o2.getQuantity().compareTo(o1.getQuantity()));
                for (int i = 0; i < data.size(); i++) {
                    if (i < 10) {
                        newData.add(data.get(i));
                    }
                }
                response.setStatus(200);
                response.setData(newData);
            } else {
                response.setMessage("Không có bản ghi nào cả");
                response.setStatus(200);
                response.setData(newData);
            }
        }
        return response;
    }

    /**
     * Description -
     *
     * @param date - Ngày cần kiểm tra
     * @return true or false
     * @author - tinhbdt
     * @created - 09/02/2022
     */
    private Long isTwoDay(String date) {
        long timeDistance = 0;
        LocalDate dateminus = LocalDate.now().minusDays(2);
        try {
            String time2 = String.valueOf(dateminus);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = format.parse(date);
            Date date2 = format.parse(time2);
            timeDistance = date2.getTime() - date1.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeDistance;
    }

}
