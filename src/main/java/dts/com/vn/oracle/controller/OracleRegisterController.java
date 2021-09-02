package dts.com.vn.oracle.controller;

import dts.com.vn.oracle.service.OracleRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oracle/register")
public class OracleRegisterController {
    private final OracleRegisterService oracleRegisterService;

    public OracleRegisterController(OracleRegisterService oracleRegisterService) {
        this.oracleRegisterService = oracleRegisterService;
    }

    private static final Logger logger = LoggerFactory.getLogger(OracleRegisterController.class);


}
