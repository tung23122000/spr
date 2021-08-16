package dts.com.vn.service;

import dts.com.vn.entities.PCRFGroup;
import dts.com.vn.entities.PrefixInfo;
import dts.com.vn.repository.PCRFGroupRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
}
