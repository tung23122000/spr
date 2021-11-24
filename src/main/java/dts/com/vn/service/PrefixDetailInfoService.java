package dts.com.vn.service;

import dts.com.vn.entities.PrefixDetailInfo;
import dts.com.vn.repository.PrefixDetailInfoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrefixDetailInfoService {
    private final PrefixDetailInfoRepository prefixDetailInfoRepository;

    public PrefixDetailInfoService(PrefixDetailInfoRepository prefixDetailInfoRepository) {
        this.prefixDetailInfoRepository = prefixDetailInfoRepository;
    }

    public Page<PrefixDetailInfo> getPrefixDetailById(Long prefixId, Pageable pageable){
        return prefixDetailInfoRepository.findAllByPrefixInfoId(prefixId, pageable);
    }

    public PrefixDetailInfo addPrefixDetail(PrefixDetailInfo prefixDetailInfo){
        return prefixDetailInfoRepository.save(prefixDetailInfo);
    }
}
