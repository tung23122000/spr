package dts.com.vn.service.serviceImpl;

import dts.com.vn.entities.InfoTargetSystem;
import dts.com.vn.repository.InfoTargetSystemRepository;
import dts.com.vn.request.InfoTargetSystemRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.InfoTargetSystemResponse;
import dts.com.vn.service.InfoTargetSystemService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class InfoTargetSystemSerivceImpl implements InfoTargetSystemService {

    private final InfoTargetSystemRepository infoTargetSystemRepository;

    public InfoTargetSystemSerivceImpl(
            InfoTargetSystemRepository infoTargetSystemRepository) {this.infoTargetSystemRepository = infoTargetSystemRepository;}


    @Override
    public ApiResponse findAllInfo() {
        ApiResponse response = new ApiResponse();
        List<InfoTargetSystemResponse> listResponse = new ArrayList<>();
        List<InfoTargetSystem> listResponseFromDb = infoTargetSystemRepository.findAll();
        if(listResponseFromDb.size() > 0){
            for (InfoTargetSystem infoTargetSystem : listResponseFromDb) {
                InfoTargetSystemResponse infoTargetSystemResponse = new InfoTargetSystemResponse();
                infoTargetSystemResponse.setId(infoTargetSystem.getId());
                infoTargetSystemResponse.setNameTargetSystem(infoTargetSystem.getNameTargetSystem());
                infoTargetSystemResponse.setIpTargetSystem(infoTargetSystem.getIpTargetSystem());
                infoTargetSystemResponse.setPortTargetSystem(infoTargetSystem.getPortTargetSystem());
                listResponse.add(infoTargetSystemResponse);
            }
            response.setMessage("Lấy dữ liệu thành công!");
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(listResponse);
        } else{
            response.setMessage("Không tồn tại bản ghi nào !");
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(listResponse);
        }
        return response;
    }

    @Override
    public ApiResponse createInfoTargetSystem(InfoTargetSystemRequest request) {
        ApiResponse response = new ApiResponse();
        InfoTargetSystem infoTargetSystem = new InfoTargetSystem();
        infoTargetSystem.setId(null);
        infoTargetSystem.setNameTargetSystem(request.getNameTargetSystem());
        infoTargetSystem.setIpTargetSystem(request.getIpTargetSystem());
        infoTargetSystem.setPortTargetSystem(request.getPortTargetSystem());
        infoTargetSystemRepository.save(infoTargetSystem);
        response.setMessage("Thêm mới bản ghi thành công!");
        response.setStatus(0);
        response.setErrorCode("00");
        return response;
    }

    @Override
    public ApiResponse getDetail(Long id) {
        ApiResponse response = new ApiResponse();
        Optional<InfoTargetSystem> infoTargetSystem = infoTargetSystemRepository.findById(id);
        if(infoTargetSystem.isPresent()){
            InfoTargetSystemResponse infoTargetSystemResponse = new InfoTargetSystemResponse();
            infoTargetSystemResponse.setId(infoTargetSystem.get().getId());
            infoTargetSystemResponse.setNameTargetSystem(infoTargetSystem.get().getNameTargetSystem());
            infoTargetSystemResponse.setIpTargetSystem(infoTargetSystem.get().getIpTargetSystem());
            infoTargetSystemResponse.setPortTargetSystem(infoTargetSystem.get().getPortTargetSystem());
            response.setMessage("Lấy chi tiết danh sách thành công!");
            response.setStatus(0);
            response.setData(infoTargetSystemResponse);
            response.setErrorCode("00");
        }else{
            response.setMessage("Không tồn tại bản ghi này!");
            response.setStatus(1);
            response.setErrorCode("00");
        }
        return response;
    }

    @Override
    public ApiResponse updateInfo(InfoTargetSystemResponse request) {
        ApiResponse response = new ApiResponse();
        Optional<InfoTargetSystem> infoTargetSystem = infoTargetSystemRepository.findById(request.getId());
        if(infoTargetSystem.isPresent()){
            infoTargetSystemRepository.updateInfoTargetSystem(request.getNameTargetSystem(),
                                                      request.getIpTargetSystem(),request.getPortTargetSystem(),request.getId());
            response.setMessage("Cập nhật bản ghi thành công!");
            response.setStatus(0);
            response.setErrorCode("00");
        }else {
            response.setMessage("Không tồn tại bản ghi này!");
            response.setStatus(1);
            response.setErrorCode("00");
        }
        return response;
    }

    @Override
    public ApiResponse deleteInfo(Long id) {
        ApiResponse response = new ApiResponse();
        Optional<InfoTargetSystem> infoTargetSystem = infoTargetSystemRepository.findById(id);
        if(infoTargetSystem.isPresent()){
            infoTargetSystemRepository.deleteById(id);
            response.setMessage("Xoá bản ghi thành công!");
            response.setStatus(0);
            response.setErrorCode("00");
        }else {
            response.setMessage("Không tồn tại bản ghi này!");
            response.setStatus(1);
            response.setErrorCode("00");
        }
        return response;
    }

    @Override
    public ApiResponse getAllSystemsNames() {
        ApiResponse response = new ApiResponse();
        List<String> listResponse = new ArrayList<>();
        List<InfoTargetSystem> listResponseFromDb = infoTargetSystemRepository.findAll();
        if(listResponseFromDb.size() > 0){
            for (InfoTargetSystem infoTargetSystem : listResponseFromDb) {
                listResponse.add(infoTargetSystem.getNameTargetSystem());
            }
            response.setMessage("Lấy dữ liệu thành công!");
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(listResponse);
        } else{
            response.setMessage("Không tồn tại bản ghi nào !");
            response.setStatus(0);
            response.setErrorCode("00");
            response.setData(listResponse);
        }
        return response;
    }

}
