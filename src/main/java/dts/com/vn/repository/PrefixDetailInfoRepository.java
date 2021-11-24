package dts.com.vn.repository;

import dts.com.vn.entities.PrefixDetailInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrefixDetailInfoRepository extends JpaRepository<PrefixDetailInfo, Long> {
    @Query("SELECT pdi FROM PrefixDetailInfo pdi WHERE pdi.prefixInfoId = ?1 ORDER BY pdi.prefixDetailId asc")
    Page<PrefixDetailInfo> findAllByPrefixInfoId(Long prefixId, Pageable pageable);
}
