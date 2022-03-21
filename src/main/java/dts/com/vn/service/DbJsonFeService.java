package dts.com.vn.service;


import dts.com.vn.request.DbJsonRequest;
import dts.com.vn.response.ApiResponse;

import java.io.IOException;

public interface DbJsonFeService {

    /**
     * Description - Hàm thay đổi dữ liệu db json và tạo ra một bản backup theo thời gian thực
     *
     * @return any
     * @author - tinhbdt
     * @created - 21/03/2022
     */
    ApiResponse changeDbJson(DbJsonRequest request);

    /**
     * Description - Hàm lấy ra danh sách file DbJson và file backup
     *
     * @return any
     * @author - tinhbdt
     * @created - 21/03/2022
     */
    ApiResponse getListDbJson() throws IOException;

    /**
     * Description - Hàm lấy ra chi tiết của file dựa theo tên file
     *
     * @return any
     * @author - tinhbdt
     * @created - 21/03/2022
     */
    ApiResponse getDetailDbJson(String name);

}
