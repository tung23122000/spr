package dts.com.vn.oracle.service;

import dts.com.vn.oracle.repository.OracleRegisterRepository;
import org.springframework.stereotype.Service;

@Service
public class OracleRegisterService {
    private final OracleRegisterRepository oracleRegisterRepository;

    public OracleRegisterService(OracleRegisterRepository oracleRegisterRepository) {
        this.oracleRegisterRepository = oracleRegisterRepository;
    }
}
