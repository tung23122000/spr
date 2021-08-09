package dts.com.vn.repository;

import dts.com.vn.entities.SubServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubServicePackageRepository extends JpaRepository<SubServicePackage, Long> {

    @Query("SELECT ssp FROM SubServicePackage ssp WHERE ssp.packageId = :packageId GROUP BY ssp.packageId, ssp.subServicePackageId")
    List<SubServicePackage> findGroupByPackageId(Long packageId);

    @Query("SELECT ssp FROM SubServicePackage ssp WHERE ssp.packageId = :packageId and ssp.subPackageId = :subPackageId")
    SubServicePackage checkExist(Long packageId, Long subPackageId);
}
