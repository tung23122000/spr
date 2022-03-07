package dts.com.vn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulerConfiguration {

	/**
	 * Description - Cron job query lấy data insert vào bảng report hàng đêm
	 *
	 * @author - giangdh
	 * @created - 07/03/2022
	 */
	@Scheduled(cron = "0/15 * * * * *")
	private void queryDataReport() {

	}

}
