package dts.com.vn.config;

import com.google.gson.Gson;
import dts.com.vn.entities.RequestSummary;
import dts.com.vn.ilink.service.CleanDataService;
import dts.com.vn.repository.RequestSummaryRepository;
import dts.com.vn.service.AutoImportNeifService;
import dts.com.vn.service.AutoImportService;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerConfiguration.class);

    private final RequestSummaryRepository requestSummaryRepository;

    private final CleanDataService dataService;

    private final AutoImportService autoImportService;

    private final AutoImportNeifService autoImportNeifService;

    @Autowired
    public SchedulerConfiguration(AutoImportService autoImportService,
                                  RequestSummaryRepository requestSummaryRepository, CleanDataService dataService,
                                  AutoImportNeifService autoImportNeifService) {
        this.autoImportService = autoImportService;
        this.requestSummaryRepository = requestSummaryRepository;
        this.dataService = dataService;
        this.autoImportNeifService = autoImportNeifService;
    }

    /**
     * Description - Cronjob chạy 1 phút 1 lần clean data từ db ilink -> db spr
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
            // Xử lý trường hợp lần đầu chạy job
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(new Date());
            Date startDate = DateUtils.addMinutes(endDate.getTime(), -1);
            startTime = new Timestamp(startDate.getTime());
            endTime = new Timestamp(endDate.getTimeInMillis());
        }
        logger.info("==========> Start date: {}, End date: {}", startTime, endTime);
        dataService.cleanIlinkData(startTime.toString(), endTime.toString());
    }

    /**
     * Description - 1.Cron job query lấy data Báo cáo tổng hợp gói cước
     *
     * @author - giangdh
     * @created - 07/03/2022
     */
    @Scheduled(cron = "0/15 * * * * *")
    private void queryDataReport1() {

    }

    /**
     * Description - 2.Cron job query lấy data Báo cáo retry gia hạn gói cước
     *
     * @author - giangdh
     * @created - 07/03/2022
     */
    @Scheduled(cron = "0/15 * * * * *")
    private void queryDataReport2() {

    }


    /**
     * Description - 3.Cron job query lấy data Báo cáo về top thuê bao thực hiện lệnh ngày hôm trước
     *
     * @author - giangdh
     * @created - 07/03/2022
     */
    @Scheduled(cron = "0/15 * * * * *")
    private void queryDataReport3() {

    }

    /**
     * Description - 4.Cron job query lấy data Báo cáo gia hạn lỗi ngày hôm trước
     *
     * @author - giangdh
     * @created - 07/03/2022
     */
    @Scheduled(cron = "0/15 * * * * *")
    private void queryDataReport4() {

    }


    /**
     * Description - Cron job query lấy data Báo cáo chốt số liệu trên hệ thống VASP theo ngày
     *
     * @author - giangdh
     * @created - 07/03/2022
     */
    @Scheduled(cron = "0/15 * * * * *")
    private void queryDataReport5() {

    }

    /**
     * Description - AutoImport file từ thư mục home/spr/import
     *
     * @author - tinhbdt
     * @created - 31/03/2022
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 6000)
    private void autoImport() {
        autoImportService.autoImport();
    }

    /**
     * Description - AutoImport neif từ file trong thư mục /home/spr/import-neif
     *
     * @author - tinhbdt
     * @created - 18/04/2022
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 9000)

    private void autoImportNeif() {
        autoImportNeifService.autoImportNeif();
    }

    /**
     * Description - Transfer file từ server khác sang thư mục /home/spr/import-neif
     *
     * @author - tinhbdt
     * @created - 18/04/2022
     */
    @Scheduled(fixedDelay = 10000, initialDelay = 3000)
    private void autoTransferFileNeif() {
        autoImportNeifService.transferFile();
    }

}
