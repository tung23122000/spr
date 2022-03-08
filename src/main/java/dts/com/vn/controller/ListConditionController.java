package dts.com.vn.controller;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.controller.LKTCheckConditionController;
import dts.com.vn.request.NewListConditionRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ListConditionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/list-condition")
public class ListConditionController {

    private static final Logger logger = LoggerFactory.getLogger(LKTCheckConditionController.class);

    private final ListConditionService listConditionService;

    @Autowired
    public ListConditionController(ListConditionService listConditionService) {
        this.listConditionService = listConditionService;
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addListCondition(@RequestBody NewListConditionRequest  newListConditionRequest) {
        ApiResponse response;
        try{
            response = listConditionService.addListCondition(newListConditionRequest);
        }catch (Exception ex){
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/update")
    public ResponseEntity<ApiResponse> updateListCondition(@RequestParam(name="id") Integer id,@RequestParam(name="isPackage") Boolean isPackage) {
        ApiResponse response ;
        try{
            response = listConditionService.updateIsPackage(id,isPackage);
        }catch (Exception ex){
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
        }
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/delete")
    public ResponseEntity<ApiResponse> deleteListCondition(@RequestParam(name="id") Integer id) {
        ApiResponse response ;
        try{
            response = listConditionService.deleteListCondition(id);
        }catch (Exception ex){
            logger.error(ex.toString());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", ex.getMessage());
        }
        return ResponseEntity.ok().body(response);
    }
}
