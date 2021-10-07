package dts.com.vn.service;

import dts.com.vn.entities.PCRFGroup;
import dts.com.vn.entities.PrefixInfo;
import dts.com.vn.repository.PCRFGroupRepository;
import dts.com.vn.response.PCRFGroupResponse;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PCRFGroupService {
    private final PCRFGroupRepository pcrfGroupRepository;

    public PCRFGroupService(PCRFGroupRepository pcrfGroupRepository) {
        this.pcrfGroupRepository = pcrfGroupRepository;
    }

    public List<PCRFGroup> getAll() {
        return pcrfGroupRepository.findAll(Sort.by(Sort.Direction.ASC, "pcrfGroupId"));
    }

    public PCRFGroup addPCRFGroup(PCRFGroup pcrfGroup){
        return pcrfGroupRepository.save(pcrfGroup);
    }

    public List<PCRFGroupResponse> findPcrfGroup(String pcrfGroupId) {
        String[] listId = pcrfGroupId.split(",");
        List<PCRFGroupResponse> returnList = new ArrayList<>();
        for (String item: listId) {
            PCRFGroup pcrfGroup = pcrfGroupRepository.getOne(Long.valueOf(item));
            PCRFGroupResponse pcrfGroupResponse = new PCRFGroupResponse(pcrfGroup);
            returnList.add(pcrfGroupResponse);
        }
        return returnList;
    }
}
