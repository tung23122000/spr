package dts.com.vn.repository;

import dts.com.vn.entities.SubPackageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SubPackageInfoRepository extends JpaRepository<SubPackageInfo, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM sub_package_info WHERE package_id = ?1 ")
    SubPackageInfo findByPackageId(Long packageId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE sub_package_info SET flex_sub_package_id = ?1 WHERE package_id = ?2")
    void upadetFlexSubPackageInfo(Long flexId,Long packageId);

}
