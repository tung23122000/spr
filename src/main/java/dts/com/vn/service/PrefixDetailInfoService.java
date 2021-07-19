package dts.com.vn.service;

import dts.com.vn.entities.PrefixDetailInfo;
import dts.com.vn.repository.PrefixDetailInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrefixDetailInfoService {
    private final PrefixDetailInfoRepository prefixDetailInfoRepository;

    public PrefixDetailInfoService(PrefixDetailInfoRepository prefixDetailInfoRepository) {
        this.prefixDetailInfoRepository = prefixDetailInfoRepository;
    }

    public List<PrefixDetailInfo> getPrefixDetailById(Long prefixId){
        return prefixDetailInfoRepository.findAllByPrefixInfoId(prefixId);
    }

    public PrefixDetailInfo addPrefixDetail(PrefixDetailInfo prefixDetailInfo){
        return prefixDetailInfoRepository.save(prefixDetailInfo);
    }
}
