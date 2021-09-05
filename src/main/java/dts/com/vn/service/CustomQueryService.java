package dts.com.vn.service;

import dts.com.vn.entities.RenewData;
import dts.com.vn.properties.AppConfigProperties;
import dts.com.vn.repository.CustomQueryRepository;
import dts.com.vn.request.RenewDataRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;
import java.io.*;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

@Service
@PersistenceContext
public class CustomQueryService {
    @Autowired
    private CustomQueryRepository customQueryRepository;

    @Autowired
    private AppConfigProperties appConfigProperties;

    public void execute(RenewDataRequest renewDataRequest) throws IOException {
        StringBuilder sql = new StringBuilder(renewDataRequest.getInputSQL());
        List<RenewData> data = getData(sql.toString());
        File path = new File(appConfigProperties.getAppConfigPath());
        if (path.listFiles().length > 0) {
            for (int i = 0; i < path.listFiles().length; i++) {
                if (path.listFiles()[i].getName().startsWith("CUSTOM_QUERY")){
                    // Do anything

                    // READ OPTION
                    InputStream is = new FileInputStream(path + "\\" + path.listFiles()[i].getName());
                    Properties prop = new Properties();
                    prop.load(is);
                    String outputFolder = String.valueOf(prop.getProperty("app.output_folder"));
                    System.out.println(outputFolder);
                    //Write FILE
                    Calendar calendar = Calendar.getInstance();
                    String folder = String.format(outputFolder);
                    int day = calendar.get(Calendar.DATE);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int year = calendar.get(Calendar.YEAR);
                    int hour = calendar.get(Calendar.HOUR_OF_DAY);
                    int minute = calendar.get(Calendar.MINUTE);
                    int second = calendar.get(Calendar.SECOND);
                    String strD = day < 10 ? ("0" + day) : (day + "");
                    String strMonth = month < 10 ? ("0" + month) : (month + "");
                    String strH = hour < 10 ? ("0" + hour) : (hour + "");
                    String strM = minute < 10 ? ("0" + minute) : (minute + "");
                    String strS = second < 10 ? ("0" + second) : (second + "");
                    String pathFileOutput = folder + "CUSTOM_QUERY" + "-" + strD + strMonth + year + "-" + strH + strM + strS + ".txt";
                    File file = new File(pathFileOutput);
                    file.getParentFile().mkdirs();
                    FileWriter writer = new FileWriter(file, true);
                    writeDataToTXT(data, renewDataRequest.getInputTransactionCode(), writer);
                    writer.flush();
                    writer.close();
                }
            }
        }
    }

    private List<RenewData> getData(String sql) {
        // return this.registerRepository.selectByFilterAndSortAndPage(Register.class, sql, pageable);
        return this.customQueryRepository.selectByFilterAndSortAndPage(RenewData.class, sql);
    }

    public void writeDataToTXT(List<RenewData> listData, String transactionCode, FileWriter writer) throws IOException {
        for (RenewData renewData : listData) {
//            Hàm kiểm tra null
//          filterNull()
            if (renewData.getExtRetryNum() == null) {
                renewData.setExtRetryNum(1L);
            } else {
                renewData.setExtRetryNum(renewData.getExtRetryNum() + 1L);
            }
            writer.append(renewData.getIsdn().toUpperCase()); // ISDN, eg 909123789,999
            writer.append(",");
            writer.append(renewData.getServiceNumber()); // SERVICE_NUMBER, eg 999
            writer.append(",");
            writer.append(transactionCode + " " + renewData.getCommandCode().toUpperCase()); // COMMAND_CODE, eg GH D5
            writer.append(",,");
            writer.append(renewData.getSourceCode().toUpperCase()); // SOURCE_CODE, eg BILLING_SMS, bảng
            // external_system qua reg_ext_sys_id
            writer.append(",,,");
            writer.append(renewData.getGroupCode().toUpperCase()); // GROUP_CODE, eg NORMAL
            writer.append("\n");
        }
    }
}
