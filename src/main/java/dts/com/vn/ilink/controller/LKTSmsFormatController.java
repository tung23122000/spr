package dts.com.vn.ilink.controller;

import dts.com.vn.config.SpringFoxConfig;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(tags = {SpringFoxConfig.LKT_SMS_FORMAT_TAG})
public class LKTSmsFormatController {
}
