package dts.com.vn.ilink.service;

import dts.com.vn.ilink.dto.BstLookupTableRowRequestCustom;
import dts.com.vn.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface PackageInfoService {

	/**
	 * Description - Hàm tìm kiếm phân trang cho bảng LKT_PACKAGE_INFO
	 *
	 * @param search   - Tên lookup table
	 * @param pageable - Số trang và số bản ghi của 1 trang
	 * @return any
	 * @author - giangdh
	 * @created - 24/12/2021
	 */
	ApiResponse findAll(String search, Pageable pageable);

	/**
	 * Description - Hàm tạo mới và update 1 bản ghi cho bảng LKT_PACKAGE_INFO
	 *
	 * @param request - Thông tin mapping gói cước vói luồng trên catalog
	 * @return any
	 * @author - giangdh
	 * @created - 24/12/2021
	 */
	ApiResponse createPackageInfo(BstLookupTableRowRequestCustom request);

	/**
	 * Description - Hàm xóa 1 bản ghi cho bảng LKT_COMMERCIAL_RFS_MAPLKT_PACKAGE_INFOPING
	 *
	 * @param request - Thông tin mapping gói cước vói luồng trên catalog
	 * @return any
	 * @author - giangdh
	 * @created - 24/12/2021
	 */
	ApiResponse deletePackageInfo(BstLookupTableRowRequestCustom request);

	/**
	 * Description - Hàm tìm 1 bản ghi trong bảng bảng LKT_COMMERCIAL_RFS_MAPLKT_PACKAGE_INFOPING bằng key
	 *
	 * @param - PackageCode
	 * @return any
	 * @author - tinhbdt
	 * @created - 10/03/2022
	 */
	ApiResponse getPackageInfoByKey(String packageCode);

}
