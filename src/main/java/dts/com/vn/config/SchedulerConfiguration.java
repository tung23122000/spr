package dts.com.vn.config;

import dts.com.vn.service.AutoImportService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

	private final AutoImportService autoImportService;

	public SchedulerConfiguration(AutoImportService autoImportService) {this.autoImportService = autoImportService;}

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
	@Scheduled(fixedDelay= 10000,initialDelay= 3000)
	private void autoImport() {
		autoImportService.autoImport();
	}

}
