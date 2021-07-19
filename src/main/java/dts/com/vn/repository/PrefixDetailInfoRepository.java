package dts.com.vn.repository;

import dts.com.vn.entities.PrefixDetailInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrefixDetailInfoRepository extends JpaRepository<PrefixDetailInfo, Long> {
    List<PrefixDetailInfo> findAllByPrefixInfoId(Long prefixId);
}
