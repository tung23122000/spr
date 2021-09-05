package dts.com.vn.service;

import dts.com.vn.entities.RenewData;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.properties.AppConfigProperties;
import dts.com.vn.repository.CustomQueryRepository;
import dts.com.vn.repository.RegisterRepository;
import dts.com.vn.request.RenewDataRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.util.WriteDataUtils;
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

    @Autowired
    private WriteDataUtils writeDataUtils;

    public ApiResponse execute(RenewDataRequest renewDataRequest) throws IOException {
        ApiResponse response = null;
        List<RenewData> data = null;

        StringBuilder sql = new StringBuilder(renewDataRequest.getInputSQL());
        try {
            data = getData(sql.toString());
        }catch (Exception ex){
            ex.printStackTrace();
            return new ApiResponse(ex, ErrorCode.MISSING_DATA_FIELD);
        }
        File path = new File(appConfigProperties.getAppConfigPath());
        if (path.listFiles().length > 0) {
            for (int i = 0; i < path.listFiles().length; i++) {
                if (path.listFiles()[i].getName().startsWith("CUSTOM_QUERY")){
                    // Do anything

                    // READ OPTION
                    InputStream is = new FileInputStream(path + "/" + path.listFiles()[i].getName());
                    Properties prop = new Properties();
                    prop.load(is);
                    String outputFolder = String.valueOf(prop.getProperty("app.output_folder"));
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

                    try {
                        file.getParentFile().mkdirs();
                        FileWriter writer = new FileWriter(file, true);
                        writeDataUtils.writeDataToTXT(data, renewDataRequest.getInputTransactionCode(), writer);
                        response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), file);
                        writer.flush();
                        writer.close();
                    }catch (RestApiException ex) {
                        file.delete();
                        ex.printStackTrace();
                        throw new RestApiException(ex);
                    }
                }
            }
        }
        return response;
    }

    private List<RenewData> getData(String sql) {
        return this.customQueryRepository.selectRenewDate(sql);
    }
}
