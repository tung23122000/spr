package dts.com.vn.ilink.service;

import dts.com.vn.entities.Label;
import dts.com.vn.ilink.dto.BstLookupTableRowRequestCustom;
import dts.com.vn.response.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SmsFormatService {

	/**
	 * Description - Hàm tìm kiếm phân trang cho bảng LKT_SMS_FORMAT
	 *
	 * @return any
	 * @author - giangdh
	 * @created - 28/12/2021
	 */
	ApiResponse findAllFoFlow();

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

	/**
	 * Description - Hàm lấy danh sách label của SMS
	 *
	 * @return any
	 * @author - giangdh
	 * @created - 28/12/2021
	 */
	ApiResponse findAllLabel();

	/**
	 * Description - Hàm thêm và update label của SMS
	 *
	 * @param labels - danh sách label
	 * @return any
	 * @author - giangdh
	 * @created - 11/01/2022
	 */
	ApiResponse saveLabels(List<Label> labels);

}
