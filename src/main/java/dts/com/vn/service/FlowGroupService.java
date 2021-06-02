package dts.com.vn.service;

import dts.com.vn.entities.FlowGroup;
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.repository.FlowGroupRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class FlowGroupService {
    private final FlowGroupRepository flowGroupRepository;

    public FlowGroupService(FlowGroupRepository flowGroupRepository) {
        this.flowGroupRepository = flowGroupRepository;
    }

    public List<FlowGroup> findAll() {
        return flowGroupRepository.findAll();
    }
}
