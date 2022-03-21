package dts.com.vn.service.serviceImpl;

import com.google.gson.*;
import dts.com.vn.controller.ExternalSystemController;
import dts.com.vn.entities.KeyAndNameDbJson;
import dts.com.vn.entities.Value;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.request.DbJsonRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.DbJsonFileResponse;
import dts.com.vn.service.DbJsonFeService;
import dts.com.vn.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DbJsonServiceImpl implements DbJsonFeService {

    private static final Logger logger = LoggerFactory.getLogger(ExternalSystemController.class);

    @Override
    public ApiResponse changeDbJson(DbJsonRequest request) {
        ApiResponse response;
        if (request.getListServicePackage().size() != 0 && request.getListServicePcrf().size() != 0 && request.getListCatalogFlow().size() != 0 && request.getListConditionConfig().size() != 0 && request.getServiceProgramCode().size() != 0) {
            if (validationRequest(request)) {
                JsonObject obj;
                try {
                    FileReader reader = new FileReader("/web-admin/frontend/dist/spr-extension-config/assets/db.json");
                    //Read JSON file
                    obj = (JsonObject) JsonParser.parseReader(reader);
                    try {
                        //ghi file backup
                        writeFileBackUp(obj);
                        //ghi đè file db.json
                        writeFile(request);
                        response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), request,
                                null, "Ghi dữ liệu và tạo file back-up thành công!");
                    }catch (Exception e){
                        response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), request,
                                null, "Có lỗi xảy ra khi tạo bản ghi và ghi đè dữ liệu!");
                    }
                } catch (FileNotFoundException e) {
                    response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null,
                            null, "Không tìm thấy file này!");
                }
            } else {
                response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null,
                        null, "Key hoặc name hoặc value bị thiếu!");
            }
        } else {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null,
                    null, "Danh sách truyền lên không được để trống!");
        }
        return response;
    }

    //Ghi file backup
    private void writeFileBackUp(JsonObject obj) {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String path = "/web-admin/frontend/dist/spr-extension-config/assets/db_json_backup" + timeStamp + ".json";
            // create Gson instance with pretty-print
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            // create a writer
            Writer writer = Files.newBufferedWriter(Paths.get(path));
            // convert user object to JSON file
            gson.toJson(obj, writer);
            // close the writer
            writer.close();
        } catch (Exception ex) {
            logger.error("==========>   " + new Gson().toJson(ex.getMessage()));
        }
    }

    //ghi de len file db.json
    private void writeFile(DbJsonRequest request) {
        try {
            // create a map
            Map<String, Object> map = new HashMap<>();
            map.put("listServicePcrf", request.getListServicePcrf());
            map.put("listCatalogFlow", request.getListCatalogFlow());
            map.put("listServicePackage", request.getListServicePackage());
            map.put("serviceProgramCode", request.getServiceProgramCode());
            map.put("listConditionConfig", request.getListConditionConfig());
            // create Gson instance with pretty-print
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            // create a writer
            Writer writer = Files.newBufferedWriter(Paths.get("/web-admin/frontend/dist/spr-extension-config/assets/db.json"));
            // convert user object to JSON file
            gson.toJson(map, writer);
            // close the writer
            writer.close();
        } catch (Exception ex) {
            logger.error("==========>   " + new Gson().toJson(ex.getMessage()));
        }
    }

    //Xử lý request
    private Boolean validationRequest(DbJsonRequest request) {
        Boolean isValidKeyAndName = validateKeyAndName(request.getListServicePackage());
        Boolean isValidKeyAndName2 = validateKeyAndName(request.getListServicePcrf());
        Boolean isValidValue = validateValue(request.getServiceProgramCode());
        Boolean isValidValue2 = validateValue(request.getListCatalogFlow());
        Boolean isValidValue3 = validateValue(request.getListConditionConfig());
        return isValidKeyAndName && isValidValue2 && isValidKeyAndName2 && isValidValue && isValidValue3;
    }

    private Boolean validateKeyAndName(List<KeyAndNameDbJson> lst) {
        Boolean isTrue = null;
        for (KeyAndNameDbJson keyAndNameDbJson : lst) {
            if (keyAndNameDbJson.getKey().length() != 0 && keyAndNameDbJson.getName().length() != 0) {
                isTrue = true;
            } else {
                isTrue = false;
                break;
            }
        }
        return isTrue;
    }

    private Boolean validateValue(List<Value> lst) {
        Boolean isTrue = null;
        for (Value value : lst) {
            if (value.getValue().length() != 0) {
                isTrue = true;
            } else {
                isTrue = false;
                break;
            }
        }
        return isTrue;
    }

    @Override
    public ApiResponse getListDbJson() throws IOException {
        ApiResponse response;
        List<DbJsonFileResponse> result = new ArrayList<>();
        try {
            List<File> files = Files.list(Paths.get("/web-admin/frontend/dist/spr-extension-config/assets/"))
                    .map(Path::toFile)
                    .collect(Collectors.toList());
            for (File item : files) {
                if (item.getName().startsWith("db") && item.getName().endsWith(".json")) {
                    DbJsonFileResponse file = new DbJsonFileResponse();
                    file.setName(item.getName());
                    SimpleDateFormat df = new SimpleDateFormat(DateTimeUtil.DD_MM_YYYY_HH_mm_ss);
                    String date = df.format(new Date(item.lastModified()));
                    file.setCreateDate(date);
                    result.add(file);
                }
            }
            result.sort((o1, o2) -> o2.getCreateDate().compareTo(o1.getCreateDate()));
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), result,
                    null, "Lấy danh sách thành công!");
        } catch (FileNotFoundException e) {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null,
                    null, "Không tìm thấy file này!");
        }
        return response;
    }

    @Override
    public ApiResponse getDetailDbJson(String name) {
        ApiResponse response;
        JsonObject obj;
        Gson gson = new Gson();
        try {
            String path = "/web-admin/frontend/dist/spr-extension-config/assets/" + name;
            FileReader reader = new FileReader(path);
            //Read JSON file
            obj = (JsonObject) JsonParser.parseReader(reader);
            String gjson = gson.toJson(obj);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), gjson,
                    null, "Lấy chi tiết dữ liệu file " + name + " thành công!");
        } catch (FileNotFoundException e) {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null,
                    null, "Không tìm thấy file này!");
        }
        return response;
    }

}
