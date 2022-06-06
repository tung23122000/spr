package dts.com.vn.service.serviceImpl;

import dts.com.vn.entities.ListCondition;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.repository.ListConditionRepository;
import dts.com.vn.request.NewListConditionRequest;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.service.ListConditionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class ListConditionServiceImpl implements ListConditionService {
    private final ListConditionRepository listConditionRepository;

    @Autowired
    public ListConditionServiceImpl(ListConditionRepository listConditionRepository) {
        this.listConditionRepository = listConditionRepository;
    }

    @Override
    @Transactional
    public ApiResponse addListCondition(NewListConditionRequest newListConditionRequest) {
        ApiResponse response;
        if (newListConditionRequest != null) {
            Boolean checkExistConditionByName  =
                    listConditionRepository.checkExist(newListConditionRequest.getConditionName(),newListConditionRequest.getIlinkServiceName());
            if(checkExistConditionByName){
                ListCondition listCondition = new ListCondition();
                listCondition.setId(listConditionRepository.maxId() + 1);
                listCondition.setConditionName(newListConditionRequest.getConditionName());
                listCondition.setIlinkServiceName(newListConditionRequest.getIlinkServiceName());
                listCondition.setDescription(null);
                listCondition.setIsPackage(newListConditionRequest.getIsPackage());
                listCondition.setStatus(true);
                listConditionRepository.saveAndFlush(listCondition);
                response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), newListConditionRequest, null, "Thêm mới điều kiện thành công!");
            }else{
                response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), newListConditionRequest, null, "Điều" +
                        " kiện này đã tồn tại");
            }
        } else {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), newListConditionRequest, null, "Chưa truyền vào điều kiện mới!");
        }
        return response;
    }

    @Override
    public ApiResponse updateIsPackage(Integer id, Boolean isPackage) {
        ApiResponse response;
        Optional<ListCondition> condition = listConditionRepository.findById(id);
        if (condition.isPresent()) {
            Boolean checkExistConditionByName  =
                    listConditionRepository.checkExist(condition.get().getConditionName(), condition.get()
                                                                                                    .getIlinkServiceName());
            if(checkExistConditionByName){
                listConditionRepository.updateIsPackgeById(id, isPackage);
                response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), id, null, "Cập nhật điều kiện thành công!");
            }else{
                response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), id, null, "Điều kiện này đã tồn tại!");
            }
        } else {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), id, null, "Chưa truyền vào điều kiện mới!");
        }
        return response;
    }

    @Override
    public ApiResponse deleteListCondition(Integer id, Boolean status) {
        ApiResponse response;
        if (id != null) {
            if(status){
                listConditionRepository.activeConditionById(id);
                response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), id, null, "Kích hoạt điều kiện thành công!");
            }else {
                listConditionRepository.deleteConditionById(id);
                response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), id, null, "Xóa điều kiện thành công!");
            }
        } else {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), id, null, "Chưa truyền vào điều kiện cần xóa!");
        }
        return response;
    }
}
