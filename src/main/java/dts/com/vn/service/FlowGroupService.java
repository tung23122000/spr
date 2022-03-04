package dts.com.vn.service;

import dts.com.vn.entities.FlowGroup;
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.repository.FlowGroupRepository;
import dts.com.vn.response.ApiResponse;
import dts.com.vn.response.FlowGroupResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FlowGroupService {
    private final FlowGroupRepository flowGroupRepository;

    public FlowGroupService(FlowGroupRepository flowGroupRepository) {
        this.flowGroupRepository = flowGroupRepository;
    }

    public List<FlowGroupResponse> findAll() {
        List<FlowGroupResponse> data = new ArrayList<>();
        List<FlowGroup> flowGroup = flowGroupRepository.findAll();
        for (int i = 0; i < flowGroup.size(); i++) {
            FlowGroupResponse flowGroupResponse = new FlowGroupResponse();
            String flowCatalogName="TP_"+flowGroup.get(i).getFlowGroupName().toUpperCase().replaceAll(" ","").replaceAll("_","");
            flowGroupResponse.setFlowGroupId(flowGroup.get(i).getFlowGroupId());
            flowGroupResponse.setFlowGroupName(flowGroup.get(i).getFlowGroupName());
            flowGroupResponse.setFlowCatalogName(flowCatalogName);
            data.add(flowGroupResponse);
        }
        return data;
    }
}
