package dts.com.vn.controller;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.Constant;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.PackageNameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PackageNameFileController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalSystemController.class);

    private final PackageNameService packageNameService;

    public PackageNameFileController(
            PackageNameService packageNameService) {this.packageNameService = packageNameService;}

    @GetMapping("/getpackageName")
    public ResponseEntity<?> getPackageNameS(){
        ApiResponse response;
        try {
            String packagename = packageNameService.packagenameText();
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), packagename, null, Constant.UPLOAD_FILE_SUCCESS);
            logger.info(response.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", Constant.UPLOAD_FILE_FAIL);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/editPackageName")
    public ResponseEntity<?> editPackageNameS(@RequestBody String s){
        ApiResponse response;
        try {
            String packagename = packageNameService.editPackageNameText(s);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), packagename, null, Constant.UPLOAD_FILE_SUCCESS);
            logger.info(response.toString());
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            logger.error(e.getMessage());
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, "00", Constant.UPLOAD_FILE_FAIL);
            return ResponseEntity.badRequest().body(response);
        }
    }
}
