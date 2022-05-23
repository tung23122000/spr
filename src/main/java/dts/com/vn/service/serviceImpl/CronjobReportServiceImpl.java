package dts.com.vn.service.serviceImpl;

import com.google.gson.Gson;
import dts.com.vn.entities.*;
import dts.com.vn.repository.*;
import dts.com.vn.response.*;
import dts.com.vn.service.AutoSendEmailReportService;
import dts.com.vn.service.CronjobReportService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CronjobReportServiceImpl implements CronjobReportService {

    private final ServicePackageRepository servicePackageRepository;

    private final ServiceTypeRepository serviceTypeRepository;

    private final ListPackageResponseRepository listPackageResponseRepository;

    private final ReportsRepository reportsRepository;

    private final IsdnRetryExtendRepository isdnRetryExtendRepository;

    private final RequestSummaryRepository requestSummaryRepository;

    private final Report4Repository report4Repository;

    private final Report5Repository report5Repository;

    private final AutoSendEmailReportService autoSendEmailReportService;

    @PersistenceContext
    private EntityManager entityManager;

    public CronjobReportServiceImpl(ServicePackageRepository servicePackageRepository,
                                    ServiceTypeRepository serviceTypeRepository,
                                    ListPackageResponseRepository listPackageResponseRepository,
                                    ReportsRepository reportsRepository,
                                    IsdnRetryExtendRepository isdnRetryExtendRepository,
                                    RequestSummaryRepository requestSummaryRepository,
                                    Report4Repository report4Repository,
                                    Report5Repository report5Repository,
                                    AutoSendEmailReportService autoSendEmailReportService) {
        this.servicePackageRepository = servicePackageRepository;
        this.serviceTypeRepository = serviceTypeRepository;
        this.listPackageResponseRepository = listPackageResponseRepository;
        this.reportsRepository = reportsRepository;
        this.isdnRetryExtendRepository = isdnRetryExtendRepository;
        this.requestSummaryRepository = requestSummaryRepository;
        this.report4Repository = report4Repository;
        this.report5Repository = report5Repository;
        this.autoSendEmailReportService = autoSendEmailReportService;
    }

    /**
     * Description - Lấy dữ liệu report số 1 hàng đêm và insert vào bảng Reports
     *
     * @author - tinhbdt
     * @created - 21/04/2022
     */
    @Override
    public void insertReport1() {
        String dateNow = String.valueOf(LocalDate.now().minusDays(1));
        System.out.println("Bắt đầu xử lý report1");
        List<Long> listServiceTypeId = serviceTypeRepository.findListServiceTypeId();
        List<DailyReportResponse> listInsertToDb = new ArrayList<>();
        //Số lượng items default mỗi page
        int defaultNumberOfPages = 10;
        int totalPage = listServiceTypeId.size() / defaultNumberOfPages + 2;
        for (int i = 1; i < totalPage; i++) {
            // Chia luồng để lấy ra dữ liệu của từng nhóm gói sau đó insert vào DB
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
        Reports checkExist = reportsRepository.getReport1(Timestamp.valueOf(dateNow + " 23:59:59"));
        //autoSendEmailReportService.autoSendMailReport1(reportsInsert);
        if (checkExist == null) {
            reportsRepository.save(reports);
        }
    }

    // Lấy dữ liệu từ db theo trang để giảm tải cho hệ thống, mỗi trang gồm 10 bản ghi
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

    //Lấy dữ liệu từ db bao gồm tên gói cước, mã gói cước, số lượng thuê bao còn hiệu lực
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

    //Lấy số lượng thuê bao còn hiệu lực từ bảng register theo package_id và date
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

    //Tìm tổng số thuê bao đang có gói còn hiệu lực
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
        for (Long aLong : listResponse) amout += aLong;
        return amout;
    }

    /**
     * Description - Lấy dữ liệu report số 2 hàng đêm và insert vào bảng Reports
     *
     * @author - tinhbdt
     * @created - 21/04/2022
     */
    @Override
    public void insertReport2() {
        String dateNow = String.valueOf(LocalDate.now().minusDays(1));
        System.out.println("Bắt đầu xử lý report2");
        Timestamp startTime = Timestamp.valueOf(dateNow + " 00:00:00");
        Timestamp endTime = Timestamp.valueOf(dateNow + " 23:59:59");
        Long totalPackageNeedRetry = isdnRetryExtendRepository.getTotalIsdnNeedRetry(startTime, endTime);
        Long totalPackageRetried = isdnRetryExtendRepository.getTotalIsdnRetried(startTime, endTime);
        Report2Response response = new Report2Response();
        response.setRatioRetriedAndRetry(calculatePercentage(totalPackageRetried, totalPackageNeedRetry) + "%");
        response.setTotalNeedRetry(totalPackageNeedRetry);
        response.setTotalRetried(totalPackageRetried);
        Gson gson = new Gson();
        Reports reports = new Reports();
        reports.setReportDate(Timestamp.valueOf(dateNow + " 23:59:59"));
        reports.setReportType(2);
        reports.setInsertAt(Timestamp.valueOf(dateNow + " 23:59:59"));
        reports.setReportData(gson.toJson(response));
        Reports checkExist = reportsRepository.getReport2(endTime);
        //autoSendEmailReportService.autoSendMailReport2(response);
        if (checkExist == null) {
            reportsRepository.save(reports);
        }
    }

    //Tính toán tỷ lệ phần trăm gói cước được retry
    public double calculatePercentage(double obtained, double total) {
        if (obtained > 0 && total > 0) {
            return obtained * 100 / total;
        } else {
            return 0;
        }
    }

    /**
     * Description - Lấy dữ liệu report số 3 hàng đêm và insert vào bảng Reports
     *
     * @author - tinhbdt
     * @created - 22/04/2022
     */
    @Override
    public void insertReport3() {
        String dateNow = String.valueOf(LocalDate.now().minusDays(1));
        System.out.println("Bắt đầu xử lý report3" + Instant.now());
        Timestamp startTime = Timestamp.valueOf(dateNow + " 00:00:00");
        Timestamp endTime = Timestamp.valueOf(dateNow + " 23:59:59");
        List<Report3Response> listRe3res = new ArrayList<>();
        List<Report3FromDbResponse> listFromQuery = requestSummaryRepository.getDataFromDbByIsdnAndDate(startTime, endTime);
        for (Report3FromDbResponse report3FromDbResponse : listFromQuery) {
            Report3Response response = new Report3Response();
            response.setPhoneNumber(report3FromDbResponse.getisdn());
            response.setQuantity(report3FromDbResponse.getcount());
            response.setSourceContent(report3FromDbResponse.getchannel());
            response.setCommand(report3FromDbResponse.getrequestcommand());
            listRe3res.add(response);
        }
        Gson gson = new Gson();
        Reports reports = new Reports();
        reports.setReportDate(endTime);
        reports.setReportType(3);
        reports.setInsertAt(endTime);
        reports.setReportData(gson.toJson(listRe3res));
        Reports checkExist = reportsRepository.getReport3(endTime);
        System.out.println("Hoàn thành xử lý report3" + Instant.now());
        //autoSendEmailReportService.autoSendMailReport3(listRe3res);
        if (checkExist == null) {
            reportsRepository.save(reports);
        }
    }

    /**
     * Description - Lấy dữ liệu report số 4 hàng đêm và insert vào bảng Reports
     *
     * @author - tinhbdt
     * @created - 21/04/2022
     */
    @Override
    public void insertReport4() {
        String dateNow = String.valueOf(LocalDate.now().minusDays(1));
        System.out.println("Bắt đầu xử lý report4");
        Gson gson = new Gson();
        Reports reports = new Reports();
        reports.setReportDate(Timestamp.valueOf(dateNow + " 23:59:59"));
        reports.setReportType(4);
        reports.setInsertAt(Timestamp.valueOf(dateNow + " 23:59:59"));
        reports.setReportData(gson.toJson(getReport4Response()));
        Reports checkExist = reportsRepository.getReport4(Timestamp.valueOf(dateNow + " 23:59:59"));
        //autoSendEmailReportService.autoSendMailReport4(getReport4Response());
        if (checkExist == null) {
            reportsRepository.save(reports);
        }
    }

    //Lấy dữ liệu report số 4
    private List<Report4Response> getReport4Response() {
        List<ServicePackage> listAllPackage = servicePackageRepository.findAll();
        List<Report4Response> responseFromDb = new ArrayList<>();
        int requestSize = listAllPackage.size();
        if (requestSize > 0) {
            List<CompletableFuture<Report4Response>> listResponse = new ArrayList<>();
            CompletableFuture<Report4Response> future;
            for (int i = 0; i < listAllPackage.size(); i++) {
                int finalI = i;
                future = CompletableFuture.supplyAsync(() -> {
                    Report4Response report4Response = new Report4Response();
                    Long totalExtRtryThree = getTotalExtRtryThreeOrRetryYet(listAllPackage.get(finalI)
                                                                                          .getPackageId()
                                                                                          .toString(), true);
                    Long totalNotRetryYet = getTotalExtRtryThreeOrRetryYet(listAllPackage.get(finalI)
                                                                                         .getPackageId()
                                                                                         .toString(), false);
                    report4Response.setPackageCode(listAllPackage.get(finalI).getCode());
                    report4Response.setTotalExtRetryEqualThree(totalExtRtryThree);
                    report4Response.setTotalNotProcessedyet(totalNotRetryYet);
                    return report4Response;
                });
                listResponse.add(future);
            }
            responseFromDb = listResponse.stream()
                                         .map(CompletableFuture::join)
                                         .collect(Collectors.toList());
        }
        return responseFromDb;
    }

    //Dữ liệu về gia hạn ext_retry_num = 3(gia hạn lỗi), ext_retry_num != 3 (chưa gia hạn)
    private Long getTotalExtRtryThreeOrRetryYet(String packageId, Boolean type) {
        List<CompletableFuture<Long>> listCount = new ArrayList<>();
        CompletableFuture<Long> future;
        for (int i = 1; i < 10; i++) {
            int numberOfPartition = i;
            if (type) {
                future = CompletableFuture.supplyAsync(() -> report4Repository.getTotalExtRtryThree(entityManager,
                                                                                                    "\"" + "PART" + numberOfPartition + "\"", packageId));
                listCount.add(future);
            } else {
                future = CompletableFuture.supplyAsync(() -> report4Repository.getTotalNotRetryYet(entityManager,
                                                                                                   "\"" + "PART" + numberOfPartition + "\"", packageId));
                listCount.add(future);
            }
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
     * Description - Lấy dữ liệu report số 5 hàng đêm và insert vào bảng Report
     *
     * @author - tinhbdt
     * @created - 22/04/2022
     */
    @Override
    public void insertReport5() {
        String dateNow = String.valueOf(LocalDate.now().minusDays(1));
        System.out.println("Bắt đầu xử lý report5");
        Report5Response response = new Report5Response();
        Timestamp startTime = Timestamp.valueOf(dateNow + " 00:00:00");
        Timestamp endTime = Timestamp.valueOf(dateNow + " 23:59:59");
        response.setMoneyMinusPerDay(getTotalChargePrice(dateNow + " 00:00:00", dateNow + " 23:59:59"));
        response.setTotalPackageActive(findAllPhoneNumberHaveActivePackage());
        response.setTotalPackageWaitRetry(isdnRetryExtendRepository.getTotalIsdnWaitRetry(startTime, endTime));
        response.setTotalPackageDelete(getTotalPackageDeleteOrActive(true));
        response.setTotalPackageActiveOrRenew(getTotalPackageDeleteOrActive(false));
        Gson gson = new Gson();
        //Vì chưa có phần api check chéo chạy giữa 2 server nên chú ý khi deploy chỉ đẩy phần này lên 1 trong 2 con của prod
        //autoSendEmailReportService.autoSendMailReport5(response);
        Reports reports = new Reports();
        reports.setReportDate(endTime);
        reports.setReportType(5);
        reports.setInsertAt(endTime);
        reports.setReportData(gson.toJson(response));
        Reports checkExist = reportsRepository.getReport5(endTime);
        if (checkExist == null) {
            reportsRepository.save(reports);
        }
    }

    // Lấy tổng số gói cước được đăng ký/ gia hạn hoặc huỷ theo ngày bắt đầu tháng tới ngày báo cáo
    private Long getTotalPackageDeleteOrActive(Boolean type) {
        List<CompletableFuture<Long>> listCount = new ArrayList<>();
        CompletableFuture<Long> future;
        for (int i = 1; i < 10; i++) {
            int numberOfPartition = i;
            if (type) {
                future = CompletableFuture.supplyAsync(() -> report5Repository.getTotalPackageDelete(entityManager,
                                                                                                     "\"" + "PART" + numberOfPartition + "\""));
                listCount.add(future);
            } else {
                future = CompletableFuture.supplyAsync(() -> report5Repository.getTotalPackageActiveOrRenew(entityManager,
                                                                                                            "\"" + "PART" + numberOfPartition + "\""));
                listCount.add(future);
            }
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

    //Lấy số tiền trừ theo ngày (anh Khánh confirm)
    private Long getTotalChargePrice(String startTime, String endTime) {
        List<CompletableFuture<Long>> listCount = new ArrayList<>();
        CompletableFuture<Long> future;
        for (int i = 1; i < 10; i++) {
            int numberOfPartition = i;
            future = CompletableFuture.supplyAsync(() -> report5Repository.getTotalChargePrice(entityManager,
                                                                                               "\"" + "PART" + numberOfPartition + "\"", startTime, endTime));
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

}
