package dts.com.vn.util;

import dts.com.vn.entities.RenewData;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
public class WriteDataUtils {

    @Autowired
    private RegisterRepository registerRepository;

    public void writeDataToTXT(List<RenewData> listData, String transactionCode, FileWriter writer) throws IOException {
        for (RenewData renewData : listData) {
//            Hàm kiểm tra null
            if (checkNullRenewData(renewData)){
                if (transactionCode.trim().equals("GH")){
                    if (renewData.getExtRetryNum() == null) {
                        renewData.setExtRetryNum(1L);
                    } else {
                        renewData.setExtRetryNum(renewData.getExtRetryNum() + 1L);
                    }
                    registerRepository.saveRegister(renewData.getRegId(), renewData.getExtRetryNum());
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

    public Boolean checkNullRenewData(RenewData renewData){
        if (renewData.getRegId() == null){
            throw new RestApiException(ErrorCode.REG_ID_NULL);
        }
        if (renewData.getCommandCode() == null){
            throw new RestApiException(ErrorCode.COMMAND_CODE_NULL);
        }
        if (renewData.getGroupCode() == null){
            throw new RestApiException(ErrorCode.GROUP_CODE_NULL);
        }
        if (renewData.getIsdn() == null) {
            throw new RestApiException(ErrorCode.ISDN_NULL);
        }
        if (renewData.getServiceNumber() == null){
            throw new RestApiException(ErrorCode.SERVICE_NUMBER_NULL);
        }
        if (renewData.getSourceCode() == null){
            throw new RestApiException(ErrorCode.SOURCE_CODE_NULL);
        }
        return true;
    }
}
