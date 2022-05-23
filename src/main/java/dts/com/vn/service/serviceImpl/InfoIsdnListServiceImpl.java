package dts.com.vn.service.serviceImpl;

import dts.com.vn.entities.InfoIsdnList;
import dts.com.vn.entities.IsdnList;
import dts.com.vn.repository.InfoIsdnListRepository;
import dts.com.vn.repository.IsdnListRepository;
import dts.com.vn.request.InfoIsdnListRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.InfoIsdnListResponse;
import dts.com.vn.response.ListIsdnListIsResponse;
import dts.com.vn.service.InfoIsdnListService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InfoIsdnListServiceImpl implements InfoIsdnListService {

    private final InfoIsdnListRepository infoIsdnListRepository;

    private final IsdnListRepository isdnListRepository;

    public InfoIsdnListServiceImpl(
            InfoIsdnListRepository infoIsdnListRepository, IsdnListRepository isdnListRepository) {
        this.infoIsdnListRepository = infoIsdnListRepository;
        this.isdnListRepository = isdnListRepository;
    }

    /**
     * Description - Tìm tất cả các bản ghi trong bảng InfoIsdnList
     *
     * @return List InfoIsdnListResponse
     * @author - tinhbdt
     * @created - 29/04/2022
     */
    @Override
    public ApiResponse findAllInfo() {
        ApiResponse response = new ApiResponse();
        List<InfoIsdnListResponse> listResponse = new ArrayList<>();
        List<InfoIsdnList> listResponseFromDb = infoIsdnListRepository.findAll();
        for (InfoIsdnList infoIsdnList : listResponseFromDb) {
            InfoIsdnListResponse infoIsdnListResponse = new InfoIsdnListResponse();
            infoIsdnListResponse.setId(infoIsdnList.getId());
            infoIsdnListResponse.setNameIsdnList(infoIsdnList.getNameIsdnList());
            infoIsdnListResponse.setNameTargetFolder(infoIsdnList.getNameTargetFolder());
            infoIsdnListResponse.setNameTargetSystem(infoIsdnList.getNameTargetSystem());
            infoIsdnListResponse.setUser(infoIsdnList.getUser());
            infoIsdnListResponse.setPassword(infoIsdnList.getPassword());
            infoIsdnListResponse.setIsdnListId(infoIsdnList.getIsdnListId());
            listResponse.add(infoIsdnListResponse);
        }
        response.setMessage("Lấy dữ liệu thành công!");
        response.setStatus(0);
        response.setErrorCode("00");
        response.setData(listResponse);
        return response;
    }

    /**
     * Description - Tạo mới bản ghi trong bảng InfoIsdnList
     *
     * @param - InfoIsdnListRequest
     * @author - tinhbdt
     * @created - 29/04/2022
     */
    @Override
    public ApiResponse createInfo(InfoIsdnListRequest request) {
        ApiResponse response = new ApiResponse();
        Optional<IsdnList> isdnList = isdnListRepository.findById(request.getIsdnListId());
        if (checkExistInServer(request.getIsdnListId()) && isdnList.isPresent()) {
            InfoIsdnList infoIsdnList = new InfoIsdnList();
            infoIsdnList.setIsdnListId(isdnList.get().getIsdnListId());
            infoIsdnList.setNameIsdnList(isdnList.get().getName());
            infoIsdnList.setId(null);
            infoIsdnList.setNameTargetFolder(request.getNameTargetFolder());
            infoIsdnList.setNameTargetSystem(request.getNameTargetSystem());
            infoIsdnList.setUser(request.getUser());
            infoIsdnList.setPassword(request.getPassword());
            infoIsdnList.setIsdnListId(request.getIsdnListId());
            infoIsdnListRepository.save(infoIsdnList);
            response.setMessage("Lưu dữ liệu thành công!");
            response.setStatus(0);
            response.setErrorCode("00");
        } else {
            response.setMessage("Không tồn tại danh sách đối tượng này trên server!");
            response.setStatus(1);
            response.setErrorCode("00");
        }
        return response;
    }

    //Check xem đã có tồn tại folder ứng với isdnListId này trên server hay chưa
    private Boolean checkExistInServer(Long isdnListId) {
        List<File> files = null;
        Boolean exists = null;
        try {
            // Lấy ra tất cả các folder từ đường dẫn /home/spr/import
            try (Stream<Path> stream = Files.list(Paths.get("/home/spr/import"))) {
                files = stream.map(Path::toFile).collect(Collectors.toList());
            }
            if (files.size() > 0) {
                for (File file : files) {
                    if (isdnListId.toString().equals(file.getName())) {
                        exists = true;
                        break;
                    } else {
                        exists = false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * Description - Chi tiết một bản ghi trong bảng InfoIsdnList
     *
     * @param - id
     * @return - InfoIsdnListResponse
     * @author - tinhbdt
     * @created - 29/04/2022
     */
    @Override
    public ApiResponse getDetailInfo(Long id) {
        ApiResponse response = new ApiResponse();
        Optional<InfoIsdnList> infoIsdnList = infoIsdnListRepository.findById(id);
        if (infoIsdnList.isPresent()) {
            InfoIsdnListResponse infoIsdnListResponse = new InfoIsdnListResponse();
            infoIsdnListResponse.setId(infoIsdnList.get().getId());
            infoIsdnListResponse.setNameIsdnList(infoIsdnList.get().getNameIsdnList());
            infoIsdnListResponse.setNameTargetSystem(infoIsdnList.get().getNameTargetSystem());
            infoIsdnListResponse.setNameTargetFolder(infoIsdnList.get().getNameTargetFolder());
            infoIsdnListResponse.setUser(infoIsdnList.get().getUser());
            infoIsdnListResponse.setPassword(infoIsdnList.get().getPassword());
            infoIsdnListResponse.setIsdnListId(infoIsdnList.get().getIsdnListId());
            response.setMessage("Lấy chi tiết danh sách thành công!");
            response.setStatus(0);
            response.setData(infoIsdnListResponse);
            response.setErrorCode("00");
        } else {
            response.setMessage("Không tồn tại bản ghi này!");
            response.setStatus(1);
            response.setErrorCode("00");
        }
        return response;
    }

    /**
     * Description - Cập nhật một bản ghi trong bảng InfoIsdnList
     *
     * @param - InfoIsdnListResponse
     * @author - tinhbdt
     * @created - 29/04/2022
     */
    @Override
    public ApiResponse updateInfo(InfoIsdnListResponse request) {
        ApiResponse response = new ApiResponse();
        Optional<InfoIsdnList> infoIsdnList = infoIsdnListRepository.findById(request.getId());
        if (infoIsdnList.isPresent()) {
            infoIsdnListRepository.updateInfoIsdnList(request.getNameIsdnList(), request.getNameTargetFolder(),
                                                      request.getNameTargetSystem(),
                                                      request.getUser(), request.getPassword(), request.getId(),
                                                      request.getIsdnListId());
            response.setMessage("Cập nhật bản ghi thành công!");
            response.setStatus(0);
            response.setErrorCode("00");
        } else {
            response.setMessage("Không tồn tại bản ghi này!");
            response.setStatus(1);
            response.setErrorCode("00");
        }
        return response;
    }

    /**
     * Description - Cập nhật một bản ghi trong bảng InfoIsdnList
     *
     * @param - id
     * @author - tinhbdt
     * @created - 29/04/2022
     */
    @Override
    public ApiResponse deleteInfo(Long id) {
        ApiResponse response = new ApiResponse();
        Optional<InfoIsdnList> infoIsdnList = infoIsdnListRepository.findById(id);
        if (infoIsdnList.isPresent()) {
            infoIsdnListRepository.deleteById(id);
            response.setMessage("Xoá bản ghi thành công!");
            response.setStatus(0);
            response.setErrorCode("00");
        } else {
            response.setMessage("Không tồn tại bản ghi này!");
            response.setStatus(1);
            response.setErrorCode("00");
        }
        return response;
    }

    /**
     * Description - Lấy danh sách các folder đang có trên server
     *
     * @author - tinhbdt
     * @created - 04/05/2022
     */
    @Override
    public ApiResponse getListIsdnListId() {
        ApiResponse response = new ApiResponse();
        List<ListIsdnListIsResponse> listNameOfFile = new ArrayList<ListIsdnListIsResponse>();
        List<File> files = null;
        try {
            // Lấy ra tất cả các folder từ đường dẫn /home/spr/import
            try (Stream<Path> stream = Files.list(Paths.get("/home/spr/import"))) {
                files = stream.map(Path::toFile).collect(Collectors.toList());
            }
            if (files.size() > 0) {
                for (File file : files) {
                    if (!file.getName().equalsIgnoreCase("used")) {
                        IsdnList isdnList = isdnListRepository.findByIsdnListId(Long.valueOf(file.getName()));
                        if(isdnList!=null){
                            ListIsdnListIsResponse listIsdnListIsResponse = new ListIsdnListIsResponse();
                            listIsdnListIsResponse.setFileName(isdnList.getIsdnListId().toString());
                            listIsdnListIsResponse.setNameOfList(isdnList.getName());
                            listNameOfFile.add(listIsdnListIsResponse);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setMessage("Lấy danh sách folder thành công!");
        response.setData(listNameOfFile);
        response.setStatus(0);
        response.setErrorCode("00");
        return response;
    }

}
