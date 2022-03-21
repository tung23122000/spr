package dts.com.vn.controller;


import com.google.gson.Gson;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.request.DbJsonRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.DbJsonFeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ds-json")
public class DbJsonFe {

    private static final Logger logger = LoggerFactory.getLogger(ExternalSystemController.class);

    private final DbJsonFeService dbJsonFeService;

    @Autowired
    public DbJsonFe(DbJsonFeService dbJsonFeService) {
        this.dbJsonFeService = dbJsonFeService;
    }

    @PostMapping("/change-db-json")
    public ApiResponse changeDbJson(@RequestBody DbJsonRequest request) {
        ApiResponse response ;
        try {
           response = dbJsonFeService.changeDbJson(request);
        } catch (Exception e) {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", e.getMessage());
            logger.error("==========>   " + new Gson().toJson(response));
        }
        return response;
    }

    @GetMapping("/get-list-db-json")
    public ApiResponse getListDbJson(){
        ApiResponse response ;
        try {
            response = dbJsonFeService.getListDbJson();
        } catch (Exception e) {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", e.getMessage());
            logger.error("==========>   " + new Gson().toJson(response));
        }
        return response;
    }

    @PostMapping("/get-detail-db-json")
    public ApiResponse getDetailDbJson(@RequestBody String name) {
        ApiResponse response ;
        try {
            response = dbJsonFeService.getDetailDbJson(name);
        } catch (Exception e) {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", e.getMessage());
            logger.error("==========>   " + new Gson().toJson(response));
        }
        return response;
    }

}
