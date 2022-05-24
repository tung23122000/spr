package dts.com.vn.ilink.service;

public interface CleanDataService {

    /**
     * Description - Hàm làm sạch dữ liệu của ilink
     *
     * @param strStartDate - Thời gian bắt đầu
     * @param strEndDate   - Thời gian kết thúc
     * @author - giangdh
     * @created - 4/13/2022
     */
    void cleanIlinkData(String strStartDate, String strEndDate);

    /**
     * Description - Hàm làm sạch dữ liệu của ilarc
     *
     * @param startRequestId - RequestId bắt đầu
     * @param endRequestId   - RequestId kết thúc
     * @author - giangdh
     * @created - 4/20/2022
     */
    void cleanArchiveData(Integer startRequestId, Integer endRequestId);

    /**
     * Description - Hàm lấy thêm short code của từng request
     *
     * @param startRequestId - RequestId bắt đầu
     * @param endRequestId   - RequestId kết thúc
     * @author - giangdh
     * @created - 4/20/2022
     */
    void getShortcode(Integer startRequestId, Integer endRequestId);

}
