package dts.com.vn.service;

import dts.com.vn.entities.*;
import dts.com.vn.repository.*;
import dts.com.vn.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class ReportService {

    private final ReportsRepository reportsRepository;

    @Autowired
    public ReportService(ReportsRepository reportsRepository) {
        this.reportsRepository = reportsRepository;
    }


    /**
     * Description - Báo cáo hệ thống vasp
     *
     * @param - date: truyền vào ngày muốn tra cứu
     * @return - reports : bao gồm các thông tin của phần báo cáo hệ thống VASP
     * @author - tinhbdt
     * @created - 12/04/2022
     */
    public ApiResponse getReport1(String date) {
        ApiResponse response = new ApiResponse();
        Reports reports = reportsRepository.getReport1(Timestamp.valueOf(date + " 23:59:59"));
        if (reports != null) {
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(reports.getReportData());
            response.setMessage("Lấy dữ liệu báo cáo thành công!");
        } else {
            response.setStatus(1);
            response.setErrorCode("00");
            response.setMessage("Không tồn tại báo cáo của ngày này!");
        }
        return response;
    }

    /**
     * Description - Xử lý báo cáo retry gia hạn gói cước ngày ... theo date truyền vào
     *
     * @param date - Ngày cần truy xuất dữ liệu
     * @return any
     * @author - tinhbdt
     * @created - 20/04/2022
     */
    public ApiResponse getReport2(String date){
        ApiResponse response = new ApiResponse();
        Reports reports = reportsRepository.getReport2(Timestamp.valueOf(date + " 23:59:59"));
        if (reports != null) {
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(reports.getReportData());
            response.setMessage("Lấy dữ liệu báo cáo thành công!");
        } else {
            response.setStatus(1);
            response.setErrorCode("00");
            response.setMessage("Không tồn tại báo cáo của ngày này!");
        }
        return response;
    }

    /**
     * Description - Xử lý báo cáo 100 số điện thoại thực hiện nhiều lệnh nhất
     *
     * @param date - Ngày cần truy xuất dữ liệu
     * @return any
     * @author - tinhbdt
     * @created - 20/04/2022
     */
    public ApiResponse getReport3(String date) {
        ApiResponse response = new ApiResponse();
        Reports reports = reportsRepository.getReport3(Timestamp.valueOf(date + " 23:59:59"));
        if (reports != null) {
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(reports.getReportData());
            response.setMessage("Lấy dữ liệu báo cáo thành công!");
        } else {
            response.setStatus(1);
            response.setErrorCode("00");
            response.setMessage("Không tồn tại báo cáo của ngày này!");
        }
        return response;
    }

    /**
     * Description - Xử lý báo cáo gia hạn lỗi ngày hôm trước
     *
     * @param date - Ngày cần truy xuất dữ liệu
     * @return any
     * @author - tinhbdt
     * @created - 20/04/2022
     */
    public ApiResponse getReport4(String date){
        ApiResponse response = new ApiResponse();
        Reports reports = reportsRepository.getReport4(Timestamp.valueOf(date + " 23:59:59"));
        if (reports != null) {
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(reports.getReportData());
            response.setMessage("Lấy dữ liệu báo cáo thành công!");
        } else {
            response.setStatus(1);
            response.setErrorCode("00");
            response.setMessage("Không tồn tại báo cáo của ngày này!");
        }
        return response;
    }

    /**
     * Description - Xử lý báo cáo chốt số liệu hệ thống
     *
     * @param date - Ngày cần truy xuất dữ liệu
     * @return any
     * @author - tinhbdt
     * @created - 20/04/2022
     */
    public ApiResponse getReport5(String date){
        ApiResponse response = new ApiResponse();
        Reports reports = reportsRepository.getReport5(Timestamp.valueOf(date + " 23:59:59"));
        if (reports != null) {
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(reports.getReportData());
            response.setMessage("Lấy dữ liệu báo cáo thành công!");
        } else {
            response.setStatus(1);
            response.setErrorCode("00");
            response.setMessage("Không tồn tại báo cáo của ngày này!");
        }
        return response;
    }

}
