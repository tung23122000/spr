package dts.com.vn.service;

import dts.com.vn.entities.Account;
import dts.com.vn.entities.PrefixInfo;
import dts.com.vn.repository.PrefixInfoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrefixInfoService {

    private final PrefixInfoRepository prefixInfoRepository;

    public PrefixInfoService(PrefixInfoRepository prefixInfoRepository) {
        this.prefixInfoRepository = prefixInfoRepository;
    }

    public List<PrefixInfo> getAllPrefix() {
        return prefixInfoRepository.findAll(Sort.by(Sort.Direction.ASC, "prefixInfoId"));
    }

    public PrefixInfo updatePrefix(PrefixInfo prefixInfo){
        return prefixInfoRepository.save(prefixInfo);
    }
}
