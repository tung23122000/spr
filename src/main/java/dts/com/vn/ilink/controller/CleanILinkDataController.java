package dts.com.vn.ilink.controller;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.service.CleanDataService;
import dts.com.vn.response.ApiResponse;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CleanILinkDataController {

    private static final Logger logger = LoggerFactory.getLogger(CleanILinkDataController.class);

    private final CleanDataService dataService;

    @Autowired
    public CleanILinkDataController(CleanDataService dataService) {this.dataService = dataService;}

    @ApiOperation(value = "Clean dữ liệu từ archive")
    @GetMapping("/clean-archive-data")
    @ResponseBody
    public ResponseEntity<ApiResponse> cleanIlinkData(@RequestParam Integer startRequestId,
                                                      @RequestParam Integer endRequestId) {
        ApiResponse response;
        try {
            dataService.cleanArchiveData(startRequestId, endRequestId);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), null, "0",
                                       "Làm sạch dữ liệu archive từ requestId " + startRequestId + " đến " + endRequestId + " thành công");
            return ResponseEntity.ok().body(response);
        } catch (Exception ex) {
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "0", ex.getMessage());
            return ResponseEntity.ok().body(response);
        }
    }

}
