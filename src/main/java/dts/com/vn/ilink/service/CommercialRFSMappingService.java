package dts.com.vn.ilink.service;

import dts.com.vn.ilink.entities.CommercialMapping;
import dts.com.vn.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface CommercialRFSMappingService {

	/**
	 * Description - Hàm tìm kiếm phân trang
	 *
	 * @param search   - Tên lookup table
	 * @param pageable - Số trang và số bản ghi của 1 trang
	 * @return any
	 * @author - giangdh
	 * @created - 23/12/2021
	 */
	ApiResponse findAll(String search, Pageable pageable);

	/**
	 * Description - Hàm tạo mới và update 1 bản ghi cho bảng LKT_COMMERCIAL_RFS_MAPPING
	 *
	 * @param request - Thông tin mapping gói cước vói luồng trên catalog
	 * @return any
	 * @author - giangdh
	 * @created - 23/12/2021
	 */
	ApiResponse createMapping(CommercialMapping request);

	/**
	 * Description - Hàm xóa 1 bản ghi cho bảng LKT_COMMERCIAL_RFS_MAPPING
	 *
	 * @param request - Thông tin mapping gói cước vói luồng trên catalog
	 * @return any
	 * @author - giangdh
	 * @created - 23/12/2021
	 */
	ApiResponse deleteMapping(CommercialMapping request);

}
