package dts.com.vn.service;

import dts.com.vn.entities.CommandandSource;
import dts.com.vn.entities.Register;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.ilarc.repository.IlArcTaskParameterRepository;
import dts.com.vn.ilink.repository.SasReTaskParameterRepository;
import dts.com.vn.repository.ListPackageResponseRepository;
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

@Service
public class ReportService {

    private static final String SOURCE_TYPE = "SOURCE_TYPE";

    private static final String SMS = "SMS";

    private final ServicePackageRepository servicePackageRepository;

    private final ServiceTypeRepository serviceTypeRepository;

    private final SasReTaskParameterRepository sasReTaskParameterRepository;

    private final IlArcTaskParameterRepository ilArcTaskParameterRepository;

    private final ListPackageResponseRepository listPackageResponseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ReportService(ServicePackageRepository servicePackageRepository,
                         ServiceTypeRepository serviceTypeRepository,
                         SasReTaskParameterRepository sasReTaskParameterRepository,
                         IlArcTaskParameterRepository ilArcTaskParameterRepository,
                         ListPackageResponseRepository listPackageResponseRepository) {
        this.servicePackageRepository = servicePackageRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.sasReTaskParameterRepository = sasReTaskParameterRepository;
        this.ilArcTaskParameterRepository = ilArcTaskParameterRepository;
        this.listPackageResponseRepository = listPackageResponseRepository;
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
     * Description - Báo cáo tổng hợp hệ thống vasp
     *
     * @param - date: truyền vào ngày muốn tra cứu
     * @param - page: truyền vào page cần hiển thị
     * @return - Report Daily Response (bao gồm tổng số trang, danh sách các nhóm gói và báo cáo thuê bao hiệu lực)
     * @author - tinhbdt
     * @created - 12/04/2022
     */
    public ApiResponse dailyReport(String date, Integer page) {
        ApiResponse response = new ApiResponse();
        List<Long> listServiceTypeId = serviceTypeRepository.findListServiceTypeId();
        List<CompletableFuture<DailyReportResponse>> listDailyReportResponse = new ArrayList<>();
        //Số lượng items default mỗi page
        int defaultNumberOfPages = 10;
        Integer totalPage = listServiceTypeId.size() / defaultNumberOfPages + 1;
        CompletableFuture<DailyReportResponse> future;
        //Lấy các phần tử từ listServiceTypeId dựa theo page
        for (int i = page * defaultNumberOfPages - defaultNumberOfPages; i < page * defaultNumberOfPages; i++) {
            int finalI = i;
            if (finalI <= (listServiceTypeId.size() - 1)) {
                future = CompletableFuture.supplyAsync(() -> getDailyReportResponse(listServiceTypeId.get(finalI),
                                                                                    date));
                listDailyReportResponse.add(future);
            }
        }
        List<DailyReportResponse> listResponse = listDailyReportResponse.stream()
                                                                        .map(CompletableFuture::join)
                                                                        .collect(Collectors.toList());
        ReportDailyResponse data = new ReportDailyResponse();
        data.setTotalPage(totalPage);
        data.setListReport(listResponse);
        response.setData(data);
        response.setStatus(200);
        return response;
    }

    /**
     * Description - Lấy dữ liệu từ db bao gồm tên gói cước, mã gói cước, số lượng thuê bao còn hiệu lực
     *
     * @param - serviceTypeId : tra cứu theo từng nhóm gói
     * @param - date : truyền vào ngày muốn tra cứu
     * @return - DailyReportResponse (trả ra một danh sách các gói trong nhóm và báo cáo thuê bao hiệu lực)
     * @author - tinhbdt
     * @created - 12/04/2022
     */
    private DailyReportResponse getDailyReportResponse(Long serviceTypeId, String date) {
        DailyReportResponse data = new DailyReportResponse();
        Optional<ServiceType> optServiceType = serviceTypeRepository.findById(serviceTypeId);
        optServiceType.ifPresent(serviceType -> data.setGroupName(serviceType.getName()));
        List<ServicePackage> listAllPackageSameGroup = servicePackageRepository.findAllByServiceTypeId(serviceTypeId);
        int requestSize = listAllPackageSameGroup.size();
        if (requestSize > 0) {
            List<CompletableFuture<ListPackageResponse>> listResponse = new ArrayList<>();
            CompletableFuture<ListPackageResponse> future;
            for (int i = 0; i < listAllPackageSameGroup.size(); i++) {
                int finalI = i;
                future = CompletableFuture.supplyAsync(() -> {
                    ListPackageResponse listPackageResponse = new ListPackageResponse();
                    Long total =
                            getTotalNumberOfSubscribers(listAllPackageSameGroup.get(finalI).getPackageId()
                                                                               .toString(),
                                                        date);
                    listPackageResponse.setPackageCode(listAllPackageSameGroup.get(finalI).getCode());
                    listPackageResponse.setPackageName(listAllPackageSameGroup.get(finalI).getName());
                    listPackageResponse.setTotalPhoneNumber(total);
                    return listPackageResponse;
                });
                listResponse.add(future);
            }
            List<ListPackageResponse> responseFromDb = listResponse.stream()
                                                                   .map(CompletableFuture::join)
                                                                   .collect(Collectors.toList());
            data.setListPackage(responseFromDb);
        }
        return data;
    }

    /**
     * Description - Lấy số lượng thuê bao còn hiệu lực từ bảng register theo package_id và date
     *
     * @param - packageCode: lấy số lượng thuê bao hiệu theo từng gói cước
     * @param - date : truyền vào ngày muốn tra cứu
     * @return totalNumberOfSubscribers(tổng số thuê bao đang dùng gói cước này)
     * @author - tinhbdt
     * @created - 12/04/2022
     */
    private Long getTotalNumberOfSubscribers(String packageCode, String date) {
        List<CompletableFuture<Long>> listCount = new ArrayList<>();
        CompletableFuture<Long> future;
        for (int i = 1; i < 10; i++) {
            int numberOfPartition = i;
            future = CompletableFuture.supplyAsync(() -> listPackageResponseRepository.getTotalNumberOfSubscribers(entityManager,
                                                                                                                   "\"" + "PART" + numberOfPartition + "\"", packageCode, date));
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

    //Tìm nguồn từ database Ilink
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
