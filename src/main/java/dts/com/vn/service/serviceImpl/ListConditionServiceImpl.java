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
            ListCondition listCondition = new ListCondition();
            listCondition.setId(listConditionRepository.maxId() + 1);
            listCondition.setConditionName(newListConditionRequest.getConditionName());
            listCondition.setIlinkServiceName(newListConditionRequest.getIlinkServiceName());
            listCondition.setDescription(null);
            listCondition.setIsPackage(newListConditionRequest.getIsPackage());
            listCondition.setStatus(true);
            listConditionRepository.saveAndFlush(listCondition);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), newListConditionRequest, null, "Thêm mới điều kiện thành công!");
        } else {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), newListConditionRequest, null, "Chưa truyền vào điều kiện mới!");
        }
        return response;
    }

    @Override
    public ApiResponse updateIsPackage(Integer id, Boolean isPackage) {
        ApiResponse response;
        if (id != null) {
            listConditionRepository.updateIsPackgeById(id, isPackage);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), id, null, "Cập nhật điều kiện thành công!");
        } else {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), id, null, "Chưa truyền vào điều kiện mới!");
        }
        return response;
    }

    @Override
    public ApiResponse deleteListCondition(Integer id) {
        ApiResponse response;
        if (id != null) {
            listConditionRepository.deleteConditionById(id);
            response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), id, null, "Xóa điều kiện thành công!");
        } else {
            response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), id, null, "Chưa truyền vào điều kiện cần xóa!");
        }
        return response;
    }
}
