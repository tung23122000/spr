package dts.com.vn.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dts.com.vn.entities.Reports;
import dts.com.vn.entities.ReportsInsert;
import dts.com.vn.repository.ReportsRepository;
import dts.com.vn.response.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.DataInput;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class AutoSendEmailReportService {

    private final ReportsRepository reportsRepository;

    public AutoSendEmailReportService(ReportsRepository reportsRepository) {this.reportsRepository = reportsRepository;}

    public void readDataFromReports(){
        autoSendMailReport1();
        autoSendMailReport2();
        autoSendMailReport3();
        autoSendMailReport4();
        autoSendMailReport5();
    }

    //Tạo content cho mail báo cáo report 1
    public void autoSendMailReport1() {
        LocalDate todaydate = LocalDate.now();
        String yesterday = String.valueOf(todaydate.minusDays(1));
        Reports reports = reportsRepository.getReport1(Timestamp.valueOf(yesterday + " 23:59:59"));
        Gson gson = new Gson();
        ReportsInsert response = gson.fromJson(String.valueOf(reports.getReportData()), ReportsInsert.class);
        StringBuilder sb = new StringBuilder();
        String start =
                "<p>Hi anh,</p>" + "<p>Em xin phép được gửi báo cáo tổng hợp gói cước " + yesterday + " :</p>";
        sb.append(start);
        String totalIsdnActive =
                "<p>Tổng số thuê bao đang có gói: <b style=\"color: red;\">" + response.getTotalIsdn() + "</b></p>";
        sb.append(totalIsdnActive);
        List<DailyReportResponse> listDailyReportResponse = response.getDailyReport();
        for (DailyReportResponse dailyReportResponse : listDailyReportResponse) {
            String serviceType = "<p>Nhóm: <b>" + dailyReportResponse.getGroupName() + "</b></p>";
            sb.append(serviceType);
            String header = "<table style=\"width:100%;\">";
            sb.append(header);
            String title = "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;background-color: #b8daff;\">" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">STT</td>" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">MÃ</td>" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">Tên</td>" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">SỐ LƯỢNG</td>" +
                    "</tr>";
            sb.append(title);
            if (dailyReportResponse.getListPackage() != null) {
                for (int i = 0; i < dailyReportResponse.getListPackage().size(); i++) {
                    String content = "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;\">" +
                            "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + (i + 1) + "</td>" +
                            "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + dailyReportResponse.getListPackage()
                                                                                                                 .get(i)
                                                                                                                 .getPackageCode() + "</td>" +
                            "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + dailyReportResponse.getListPackage()
                                                                                                                 .get(i)
                                                                                                                 .getPackageName() + "</td>" +
                            "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + dailyReportResponse.getListPackage()
                                                                                                                 .get(i)
                                                                                                                 .getTotalPhoneNumber() + "</td>" +
                            "</tr>";
                    sb.append(content);
                }
            }
            String endTable = "</table><br><br>";
            sb.append(endTable);
        }
        List<String> list = Arrays.asList("tinhbdt@dts.com.vn","linhnd@dts.com.vn");
        for (String s : list) {
            sendMail(sb, 1, s);
        }
        System.out.println("Gửi mail report 1 thành công!");
    }

    //Tạo content cho mail báo cáo report 2
    public void autoSendMailReport2() {
        LocalDate todaydate = LocalDate.now();
        String yesterday = String.valueOf(todaydate.minusDays(1));
        Reports reports = reportsRepository.getReport2(Timestamp.valueOf(yesterday + " 23:59:59"));
        Gson gson = new Gson();
        Report2Response response = gson.fromJson(String.valueOf(reports.getReportData()), Report2Response.class);
        StringBuilder sb = new StringBuilder();
        String start =
                "<p>Hi anh,</p>" + "<p>Em xin phép được gửi báo cáo retry gia hạn gói cước ngày " +
                        "nhất ngày " + yesterday + " :</p>";
        sb.append(start);
        String header = "<br><br><table style=\"width:100%;\">";
        String title = "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;background-color: #b8daff;\">" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Số vết cần retry trong ngày</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Số vết đã retry trong ngày</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Tỉ lệ chênh lệch %</td>" +
                "</tr>";
        sb.append(header);
        sb.append(title);
        String content = "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;\">" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + response.getTotalNeedRetry() +
                "</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + response.getTotalRetried() + "</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + response.getRatioRetriedAndRetry() + "</td>" +
                "</tr>";
        sb.append(content);
        String endTable = "</table>";
        sb.append(endTable);
        List<String> list = Arrays.asList("tinhbdt@dts.com.vn","linhnd@dts.com.vn");
        for (String s : list) {
            sendMail(sb, 2, s);
        }
        System.out.println("Gửi mail report 2 thành công!");
    }

    //Tạo content cho mail báo cáo report 3
    public void autoSendMailReport3()  {
        LocalDate todaydate = LocalDate.now();
        String yesterday = String.valueOf(todaydate.minusDays(1));
        Reports reports = reportsRepository.getReport3(Timestamp.valueOf(yesterday + " 23:59:59"));
        ObjectMapper mapper = new ObjectMapper();
        List<Report3Response> listReponse = new ArrayList<>();
        try{
            listReponse = Arrays.asList(mapper.readValue(reports.getReportData().toString(),
                                                                               Report3Response[].class));
        }catch(IOException e){
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        String start =
                "<p>Hi anh,</p>" + "<p>Em xin phép được gửi báo cáo top 100 số thuê bao thực hiện lệnh nhiều " +
                        "nhất ngày " + yesterday + " :</p>";
        sb.append(start);
        String header = "<br><br><table style=\"width:100%;\">";
        String title = "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;background-color: #b8daff;\">" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Số thuê bao</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Nguồn</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Cú pháp</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Số lượng</td>" +
                "</tr>";
        sb.append(header);
        sb.append(title);
        for (Report3Response report3Response : listReponse) {
            String content = "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;\">" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + report3Response
                    .getPhoneNumber() + "</td>" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + report3Response
                    .getSourceContent() + "</td>" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + report3Response
                    .getCommand() + "</td>" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + report3Response
                    .getQuantity() + "</td>" +
                    "</tr>";
            sb.append(content);
        }
        String endTable = "</table>";
        sb.append(endTable);
        List<String> list = Arrays.asList("tinhbdt@dts.com.vn","linhnd@dts.com.vn");
        for (String s : list) {
            sendMail(sb, 3, s);
        }
        System.out.println("Gửi mail report 3 thành công!");
    }

    //Tạo content cho mail báo cáo report 4
    public void autoSendMailReport4() {
        LocalDate todaydate = LocalDate.now();
        String yesterday = String.valueOf(todaydate.minusDays(1));
        Reports reports = reportsRepository.getReport4(Timestamp.valueOf(yesterday + " 23:59:59"));
        ObjectMapper mapper = new ObjectMapper();
        List<Report4Response> listReponse = new ArrayList<>();
        try{
            listReponse = Arrays.asList(mapper.readValue(reports.getReportData().toString(),
                                                         Report4Response[].class));
        }catch(IOException e){
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        String start = "<p>Hi anh,</p>" + "<p>Em xin phép được gửi báo cáo gia hạn lỗi ngày " + yesterday + " :</p>";
        sb.append(start);
        String header = "<br><br><table style=\"width:100%;\">";
        String title = "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;background-color: #b8daff;\">" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">STT</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Tên gói cước</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Gia hạn retry = 3</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Chưa xử lý gia hạn</td>" +
                "</tr>";
        sb.append(header);
        sb.append(title);
        for (int i = 0; i < listReponse.size(); i++) {
            String content = "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;\">" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + (i + 1) + "</td>" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + listReponse.get(i)
                                                                                                 .getPackageCode() + "</td>" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + listReponse.get(i)
                                                                                                 .getTotalExtRetryEqualThree() + "</td>" +
                    "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + listReponse.get(i)
                                                                                                 .getTotalNotProcessedyet() + "</td>" +
                    "</tr>";
            sb.append(content);
        }
        String endTable = "</table>";
        sb.append(endTable);
        List<String> list = Arrays.asList("tinhbdt@dts.com.vn","linhnd@dts.com.vn");
        for (String s : list) {
            sendMail(sb, 4, s);
        }
        System.out.println("Gửi mail report 4 thành công!");
    }

    //Tạo nội dung cho email báo cáo số 5
    public void autoSendMailReport5() {
        LocalDate todaydate = LocalDate.now();
        String yesterday = String.valueOf(todaydate.minusDays(1));
        String firstDayOfMonth = String.valueOf(todaydate.withDayOfMonth(1));
        Reports reports = reportsRepository.getReport5(Timestamp.valueOf(yesterday + " 23:59:59"));
        Gson gson = new Gson();
        Report5Response response = gson.fromJson(String.valueOf(reports.getReportData()), Report5Response.class);
        StringBuilder sb = new StringBuilder();
        String start = "<p>Hi anh,</p>" + "<p>Em xin phép được gửi báo cáo chốt số liệu trên hệ thống ngày " + yesterday + " :</p>";
        sb.append(start);
        String header = "<br><br><table style=\"width:100%;\">";
        String title = "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;background-color: #b8daff;\">" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">STT</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Ngày</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Thông tin</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Kết quả</td>" +
                "</tr>";
        sb.append(header);
        sb.append(title);
        String content = "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;\">" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">1</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + firstDayOfMonth + "-" + yesterday + "</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Số lượng gói cước huỷ</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + response.getTotalPackageDelete() + "</td>" +
                "</tr>"
                + "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;\">" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">2</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + firstDayOfMonth + "-" + yesterday + "</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Số lượng gói cước đăng ký/gia hạn</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + response.getTotalPackageActiveOrRenew() + "</td>" +
                "</tr>"
                + "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;\">" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">3</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + yesterday + "</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Số lượng gói cước chờ retry</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + response.getTotalPackageWaitRetry() + "</td>" +
                "</tr>"
                + "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;\">" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">4</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + yesterday + "</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Số lượng gói cước hiệu lực</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + response.getTotalPackageActive() + "</td>" +
                "</tr>"
                + "<tr style=\"border: 1px solid #dddddd; text-align: center; padding: 8px;\">" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">5</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + yesterday + "</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">Số tiền trừ theo ngày</td>" +
                "<td style=\"border: 1px solid #dddddd; text-align: center;\">" + response.getMoneyMinusPerDay() + "</td>" +
                "</tr>";
        String endTable = "</table>";
        sb.append(content);
        sb.append(endTable);
        List<String> list = Arrays.asList("tinhbdt@dts.com.vn","linhnd@dts.com.vn");
        for (String s : list) {
            sendMail(sb, 5, s);
        }
        System.out.println("Gửi mail report 5 thành công!");
    }

    private void sendMail(StringBuilder sb, Integer type, String emailTo) {
        String dateNow = String.valueOf(LocalDate.now().minusDays(1));
        System.out.println("Bắt đầu gửi mail!");

        String from = "selfcareinhn@mobifone.vn";

        //String host = "10.151.6.112";
        String host = "10.3.12.23";
        String port = "25";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.port", port);
        properties.setProperty("mail.smtp.auth", "true");

        try {
            SmtpAuthenticator authentication = new SmtpAuthenticator();
            MimeMessage message = new MimeMessage(Session.getDefaultInstance(properties, authentication));

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailTo));

            switch (type) {
                case 1:
                    message.setSubject("Báo cáo tổng hợp gói cước " + dateNow);
                    break;
                case 2:
                    //message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailTo));
                    message.setSubject("Báo cáo retry gia hạn gói cước ngày " + dateNow);
                    break;
                case 3:
                   // message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailTo));
                    message.setSubject("Báo cáo về top isdn thực hiện lệnh ngày " + dateNow);
                    break;
                case 4:
                   // message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailTo));
                    message.setSubject("Báo cáo gia hạn lỗi ngày hôm trước " + dateNow);
                    break;
                case 5:
                    //message.addRecipient(Message.RecipientType.CC, new InternetAddress(emailTo));
                    message.setSubject("Báo cáo chốt số liệu trên hệ thống ngày " + dateNow);
                    break;
            }
            //send html content
            message.setContent(sb.toString(), "text/html; charset=UTF-8");

            // Send message
            Transport.send(message);

            System.out.println("Gửi mail hoàn thành!");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

}
