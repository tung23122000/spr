package dts.com.vn.controller;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.request.RenewDataRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.CustomQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/custom-query")
public class CustomQueryController {
    @Autowired
    private CustomQueryService customQueryService;

    private static final Logger logger = LoggerFactory.getLogger(CustomQueryController.class);

    @PostMapping("/execute")
    public ResponseEntity<Object> executeQuery(@RequestBody RenewDataRequest renewDataRequest) {
        ApiResponse response = null;
        try {
            response = customQueryService.execute(renewDataRequest);
        } catch (RestApiException ex) {
            ex.printStackTrace();
            response = new ApiResponse(ex);
        } catch (IOException ex){

        }
        return ResponseEntity.ok().body(response);
    }
}
