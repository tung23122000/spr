package dts.com.vn.service;

import com.google.gson.Gson;
import dts.com.vn.entities.*;
import dts.com.vn.repository.*;
import dts.com.vn.response.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ServicePackageRepository servicePackageRepository;

    private final ServiceTypeRepository serviceTypeRepository;

    private final ListPackageResponseRepository listPackageResponseRepository;

    private final ReportsRepository reportsRepository;

    private  final RequestSummaryRepository requestSummaryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public ReportService(ServicePackageRepository servicePackageRepository,
                         ServiceTypeRepository serviceTypeRepository,
                         ListPackageResponseRepository listPackageResponseRepository,
                         ReportsRepository reportsRepository,
                         RequestSummaryRepository requestSummaryRepository) {
        this.servicePackageRepository = servicePackageRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.listPackageResponseRepository = listPackageResponseRepository;
        this.reportsRepository = reportsRepository;
        this.requestSummaryRepository = requestSummaryRepository;
    }

    /**
     * Description - Tìm tổng số thuê bao đang có gói còn hiệu lực
     * Sử dụng đa luồng để tính toán nhanh
     *
     * @return Number - Tổng số thuê bao đang có gói còn hiệu lực trên hệ thống
     * @author - giangdh
     * @created - 07/04/2022
     */
    private Long findAllPhoneNumberHaveActivePackage() {
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
     * Description - Báo cáo hệ thống vasp
     *
     * @param - date: truyền vào ngày muốn tra cứu
     * @return - reports : bao gồm các thông tin của phần báo cáo hệ thống VASP
     * @author - tinhbdt
     * @created - 12/04/2022
     */
    public ApiResponse dailyReport(String date) {
        ApiResponse response = new ApiResponse();
        Reports reports = reportsRepository.getDailyReportByDate(Timestamp.valueOf(date + " 23:59:59"));
        if (reports != null) {
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(reports.getReportData());
            response.setMessage("Lấy dữ liệu báo cáo thành công!");
        } else {
            response.setStatus(1);
            response.setErrorCode("00");
            response.setMessage("Không tồn tại báo cáo của ngày này!");
        }
        return response;
    }

    /**
     * Description - Lấy dữ liệu hàng đêm và insert vào bảng Reports
     *
     * @author - tinhbdt
     * @created - 19/04/2022
     */
    public void insertDailyReportToReports() {
        String dateNow = String.valueOf(LocalDate.now().minusDays(1));
        List<Long> listServiceTypeId = serviceTypeRepository.findListServiceTypeId();
        List<DailyReportResponse> listInsertToDb = new ArrayList<>();
        //Số lượng items default mỗi page
        int defaultNumberOfPages = 10;
        int totalPage = listServiceTypeId.size() / defaultNumberOfPages + 2;
        for (int i = 1; i < totalPage; i++) {
            List<DailyReportResponse> newList = getDailyReportResponseFromDb(i, listServiceTypeId, dateNow);
            listInsertToDb.addAll(newList);
        }
        Gson gson = new Gson();
        ReportsInsert reportsInsert = new ReportsInsert();
        reportsInsert.setDailyReport(listInsertToDb);
        reportsInsert.setTotalIsdn(findAllPhoneNumberHaveActivePackage());
        Reports reports = new Reports();
        reports.setReportDate(Timestamp.valueOf(dateNow + " 23:59:59"));
        reports.setReportType(1);
        reports.setInsertAt(Timestamp.valueOf(dateNow + " 23:59:59"));
        reports.setReportData(gson.toJson(reportsInsert));
        reportsRepository.save(reports);
    }

    /**
     * Description - Lấy dữ liệu từ db theo trang để giảm tải cho hệ thống, mỗi trang gồm 10 bản ghi
     *
     * @param - page : truyền vào trang thứ bao nhiêu
     * @param - listServiceTypeId : danh sách id của các nhóm gói
     * @param - date : thời gian cần tra cứu
     * @return - DailyReportResponse (trả ra một danh sách các gói trong nhóm và báo cáo thuê bao hiệu lực)
     * @author - tinhbdt
     * @created - 19/04/2022
     */
    private List<DailyReportResponse> getDailyReportResponseFromDb(Integer page, List<Long> listServiceTypeId,
                                                                   String date) {
        List<CompletableFuture<DailyReportResponse>> listDailyReportResponse = new ArrayList<>();
        CompletableFuture<DailyReportResponse> future;
        int defaultNumberOfPages = 10;
        //Lấy các phần tử từ listServiceTypeId dựa theo page
        for (int i = page * defaultNumberOfPages - defaultNumberOfPages; i < page * defaultNumberOfPages; i++) {
            int finalI = i;
            if (finalI <= (listServiceTypeId.size() - 1)) {
                future = CompletableFuture.supplyAsync(() -> getDailyReportResponse(listServiceTypeId.get(finalI),
                                                                                    date));
                listDailyReportResponse.add(future);
            }
        }
        return listDailyReportResponse.stream()
                                      .map(CompletableFuture::join)
                                      .collect(Collectors.toList());
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


    /**
     * Description - Xử lý báo cáo 10 số điện thoại có nhiều bản ghi fail nhất
     *
     * @param date - Ngày cần truy xuất dữ liệu
     * @return any
     * @author - tinhbdt
     * @created - 20/04/2022
     */
    public ApiResponse dailyTopIsdnReport(String date) {
        ApiResponse response = new ApiResponse();
        Reports reports = reportsRepository.getReportTopIsdnByDate(Timestamp.valueOf(date + " 23:59:59"));
        if (reports != null) {
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(reports.getReportData());
            response.setMessage("Lấy dữ liệu báo cáo thành công!");
        } else {
            response.setStatus(1);
            response.setErrorCode("00");
            response.setMessage("Không tồn tại báo cáo của ngày này!");
        }
        return response;
    }

    /**
     * Description - Xử lý báo cáo gia hạn lỗi ngày hôm trước
     *
     * @param date - Ngày cần truy xuất dữ liệu
     * @return any
     * @author - tinhbdt
     * @created - 20/04/2022
     */
    public ApiResponse reportRenewFaildYesterday(String date){
        ApiResponse response = new ApiResponse();
        Reports reports = reportsRepository.getReportRenewFailed(Timestamp.valueOf(date + " 23:59:59"));
        if (reports != null) {
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(reports.getReportData());
            response.setMessage("Lấy dữ liệu báo cáo thành công!");
        } else {
            response.setStatus(1);
            response.setErrorCode("00");
            response.setMessage("Không tồn tại báo cáo của ngày này!");
        }
        return response;
    }

    /**
     * Description - Xử lý báo cáo retry gia hạn gói cước ngày ... theo date truyền vào
     *
     * @param date - Ngày cần truy xuất dữ liệu
     * @return any
     * @author - tinhbdt
     * @created - 20/04/2022
     */
    public ApiResponse reportRetryRenewPackage(String date){
        ApiResponse response = new ApiResponse();
        Reports reports = reportsRepository.getReportRetryRenewPackage(Timestamp.valueOf(date + " 23:59:59"));
        if (reports != null) {
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(reports.getReportData());
            response.setMessage("Lấy dữ liệu báo cáo thành công!");
        } else {
            response.setStatus(1);
            response.setErrorCode("00");
            response.setMessage("Không tồn tại báo cáo của ngày này!");
        }
        return response;
    }

    /**
     * Description - Xử lý báo cáo chốt số liệu hệ thống
     *
     * @param date - Ngày cần truy xuất dữ liệu
     * @return any
     * @author - tinhbdt
     * @created - 20/04/2022
     */
    public ApiResponse reportDataSystem(String date){
        ApiResponse response = new ApiResponse();
        Reports reports = reportsRepository.getReportDataSystem(Timestamp.valueOf(date + " 23:59:59"));
        if (reports != null) {
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(reports.getReportData());
            response.setMessage("Lấy dữ liệu báo cáo thành công!");
        } else {
            response.setStatus(1);
            response.setErrorCode("00");
            response.setMessage("Không tồn tại báo cáo của ngày này!");
        }
        return response;
    }

}
