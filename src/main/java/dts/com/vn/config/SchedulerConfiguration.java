package dts.com.vn.config;

import com.google.gson.Gson;
import dts.com.vn.entities.RequestSummary;
import dts.com.vn.ilink.service.CleanDataService;
import dts.com.vn.repository.RequestSummaryRepository;
import dts.com.vn.service.*;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfiguration.class);

    AtomicInteger atomicInteger = new AtomicInteger(28044839);

    private final RequestSummaryRepository requestSummaryRepository;

    private final CleanDataService dataService;

    private final AutoImportService autoImportService;

    private final AutoImportNeifService autoImportNeifService;

    private final CronjobReportService cronjobReportService;

    private final AutoSendEmailReportService autoSendEmailReportService;

    private final CronjobTransferFileIsdnListService cronjobTransferFileIsdnListService;

    @Autowired
    public SchedulerConfiguration(AutoImportService autoImportService,
                                  RequestSummaryRepository requestSummaryRepository, CleanDataService dataService,
                                  AutoImportNeifService autoImportNeifService,
                                  CronjobReportService cronjobReportService,
                                  AutoSendEmailReportService autoSendEmailReportService,
                                  CronjobTransferFileIsdnListService cronjobTransferFileIsdnListService) {
        this.autoImportService = autoImportService;
        this.requestSummaryRepository = requestSummaryRepository;
        this.dataService = dataService;
        this.autoImportNeifService = autoImportNeifService;
        this.cronjobReportService = cronjobReportService;
        this.autoSendEmailReportService = autoSendEmailReportService;
        this.cronjobTransferFileIsdnListService = cronjobTransferFileIsdnListService;
    }

    /**
     * Description - Cronjob ch???y 5 ph??t 1 l???n clean data t??? db ilink -> db spr
     *
     * @author - giangdh
     * @created - 18/04/2022
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    private void cleanIlinkData() {
        RequestSummary lastRecord = requestSummaryRepository.findLastRequestSummary();
        Timestamp startTime, endTime;
        logger.info("==========> Last record in request log: {}", new Gson().toJson(lastRecord));
        if (lastRecord != null) {
            startTime = new Timestamp(lastRecord.getResponseDate().getTime());
            endTime = new Timestamp(new Date().getTime());
        } else {
            // X??? l?? tr?????ng h???p l???n ?????u ch???y job
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(new Date());
            Date startDate = DateUtils.addMinutes(endDate.getTime(), -1);
            startTime = new Timestamp(startDate.getTime());
            endTime = new Timestamp(endDate.getTimeInMillis());
        }
        logger.info("==========> Start date: {}, End date: {}", startTime, endTime);
        dataService.cleanIlinkData(startTime.toString(), endTime.toString());
    }

//    @Scheduled(fixedDelay = 10000, initialDelay = 5000)
//    private void cleanArchiveData() throws InterruptedException {
//        int initRequest = atomicInteger.intValue();
//        ExecutorService executorService = Executors.newFixedThreadPool(10);
//        List<Callable<Void>> callables = new ArrayList<>();
//        for (int i = 1; i < 11; i++) {
//            int finalI = i;
//            int endRequestId = initRequest + i * 100000;
//            callables.add(() -> {
//                int startRequestId = initRequest + (finalI - 1) * 100000;
//                dataService.cleanArchiveData(startRequestId, endRequestId);
//                return null;
//            });
//        }
//        executorService.invokeAll(callables);
//        atomicInteger.addAndGet(1000000);
//    }

    /**
     * Description - 1.Cron job query l???y data B??o c??o t???ng h???p g??i c?????c m???i 12h ????m
     * (Ch?? ?? khi deploy l??n server 138 th?? l??i th???i gian ch???y task l???i 30p so v???i 135)(cron = "0 0 1 * * *")
     *
     * @author - tinhbdt
     * @created - 19/04/2022
     */
    @Scheduled(cron = "0 0 1 * * *")
    private void queryDataReport1() {
        cronjobReportService.insertReport1();
    }

    /**
     * Description - g???i mail report theo file config
     *
     * @author - tinhbdt
     * @created - 19/04/2022
     */
//    @Scheduled(fixedDelay = 10000, initialDelay = 1000)
//    private void autoSendEmailReport() {
//        autoSendEmailReportService.readDataFromReports();
//    }

    /**
     * Description - Chuy???n file isdnList th??ng qua ftp
     *
     * @author - tinhbdt
     * @created - 19/04/2022
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 1000)
    private void autoTransferFile() {
        cronjobTransferFileIsdnListService.autoTransferFile();
    }

    /**
     * Description - 2.Cron job query l???y data B??o c??o retry gia h???n g??i c?????c ng??y...
     * (Ch?? ?? khi deploy l??n server 138 th?? l??i th???i gian ch???y task l???i 30p so v???i 135) (cron = "0 0 1 * * *")
     *
     * @author - tinhbdt
     * @created - 21/04/2022
     */
    @Scheduled(cron = "0 0 1 * * *")
    private void queryDataReport2() {
        cronjobReportService.insertReport2();
    }


    /**
     * Description - 3.Cron job query l???y data B??o c??o v??? top thu?? bao th???c hi???n l???nh ng??y h??m tr?????c
     * (Ch?? ?? khi deploy l??n server 138 th?? l??i th???i gian ch???y task l???i 30p so v???i 135)(cron = "0 0 1 * * *")
     *
     * @author - tinhbdt
     * @created - 22/03/2022
     */
    @Scheduled(cron = "0 0 1 * * *")
    private void queryDataReport3() {cronjobReportService.insertReport3();}

    /**
     * Description - 4.Cron job query l???y data B??o c??o gia h???n l???i ng??y h??m tr?????c
     * (Ch?? ?? khi deploy l??n server 138 th?? l??i th???i gian ch???y task l???i 30p so v???i 135)(cron = "0 0 1 * * *")
     *
     * @author - tinhbdt
     * @created - 21/04/2022
     */
    @Scheduled(cron = "0 0 1 * * *")
    private void queryDataReport4() {
        cronjobReportService.insertReport4();
    }


    /**
     * Description - Cron job query l???y data B??o c??o ch???t s??? li???u tr??n h??? th???ng VASP theo ng??y
     * (Ch?? ?? khi deploy l??n server 138 th?? l??i th???i gian ch???y task l???i 30p so v???i 135)(cron = "0 0 1 * * *")
     *
     * @author - tinhbdt
     * @created - 22/04/2022
     */
    @Scheduled(cron = "0 0 1 * * *")
    private void queryDataReport5() {
        cronjobReportService.insertReport5();
    }


    /**
     * Description - T??? ?????ng l???y d??? li???u trong file t??? th?? m???c home/spr/import insert v??o db( b???ng list_detail_new )
     *
     * @author - tinhbdt
     * @created - 31/03/2022
     */
//    @Scheduled(fixedDelay = 10000, initialDelay = 6000)
//    private void autoImport() {
//      autoImportService.autoImport();
//    }

    /**
     * Description - Transfer file t??? server kh??c sang th?? m???c /home/spr/import-neif
     *
     * @author - tinhbdt
     * @created - 18/04/2022
     */
//    @Scheduled(fixedDelay = 10000, initialDelay = 9000)
//    private void autoImportNeif() {
//        autoImportNeifService.autoImportNeif();
//    }

    /**
     * Description - Transfer file t??? server kh??c sang th?? m???c /home/spr/import-neif
     *
     * @author - tinhbdt
     * @created - 18/04/2022
     */
//    @Scheduled(fixedDelay = 10000, initialDelay = 3000)
//    private void autoTransferFileNeif() {
//        autoImportNeifService.transferFile();
//    }

}
