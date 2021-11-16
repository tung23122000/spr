package dts.com.vn.ilink.controller;

import dts.com.vn.config.SpringFoxConfig;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Api(tags = {SpringFoxConfig.LKT_COMMERCIAL_RFS_MAPPING_TAG})
public class LKTCommercialRFSMappingController {
}
