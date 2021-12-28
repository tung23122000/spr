package dts.com.vn.ilink.service;

import dts.com.vn.ilink.dto.BstLookupTableRowRequestCustom;
import dts.com.vn.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface SmsFormatService {

	/**
	 * Description - Hàm tìm kiếm phân trang cho bảng LKT_SMS_FORMAT
	 *
	 * @param search   - Tên lookup table
	 * @param pageable - Số trang và số bản ghi của 1 trang
	 * @return any
	 * @author - giangdh
	 * @created - 28/12/2021
	 */
	ApiResponse findAll(String search, Pageable pageable);

	/**
	 * Description - Hàm tạo mới và update 1 bản ghi cho bảng LKT_SMS_FORMAT
	 *
	 * @param request - Thông tin mapping gói cước vói luồng trên catalog
	 * @return any
	 * @author - giangdh
	 * @created - 28/12/2021
	 */
	ApiResponse createSMS(BstLookupTableRowRequestCustom request);

	/**
	 * Description - Hàm xóa 1 bản ghi cho bảng LKT_SMS_FORMAT
	 *
	 * @param request - Thông tin mapping gói cước vói luồng trên catalog
	 * @return any
	 * @author - giangdh
	 * @created - 28/12/2021
	 */
	ApiResponse deleteSMS(BstLookupTableRowRequestCustom request);

}
