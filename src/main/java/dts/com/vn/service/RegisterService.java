package dts.com.vn.service;

import dts.com.vn.entities.Register;
import dts.com.vn.oracle.entities.OracleRegister;
import dts.com.vn.oracle.repository.OracleRegisterRepository;
import dts.com.vn.repository.RegisterRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class RegisterService {
    private final RegisterRepository registerRepository;
    private final OracleRegisterRepository oracleRegisterRepository;

    public RegisterService(RegisterRepository registerRepository, OracleRegisterRepository oracleRegisterRepository) {
        this.registerRepository = registerRepository;
        this.oracleRegisterRepository = oracleRegisterRepository;
    }

    public void resetExtRetryNum(Long extRetryNum, Integer timeResetExtRetryNum) {
        Date date = new Date();
        date.setHours(date.getHours() - timeResetExtRetryNum);
        Timestamp timestamp = new Timestamp(date.getTime());
        List<Register> registerList = registerRepository.getAllRenew(extRetryNum, timestamp);
        for (Register register: registerList) {
            OracleRegister oracleRegister = new OracleRegister(register);
            oracleRegister.setRegId(register.getRegId());
            oracleRegister.setExtRetryNum(0L);
            oracleRegisterRepository.saveAndFlush(oracleRegister);
        }
    }
}
