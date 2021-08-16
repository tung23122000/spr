package dts.com.vn.repository;

import dts.com.vn.entities.BucketsInfo;
import dts.com.vn.entities.NdsTypeParamProgram;
import dts.com.vn.entities.ServicePackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {

	@Query("select sp from ServicePackage sp where (:search is not null and upper(sp.code) like CONCAT('%',upper(:search),'%')) "
			+ "or (:search is not null and upper(sp.name) like CONCAT('%',upper(:search),'%')) or "
			+ "(:search is not null and upper(sp.groupCode) like CONCAT('%',upper(:search),'%')) order by sp.packageId desc")
	Page<ServicePackage> findAll(@Param("search") String search, Pageable pageable);

	@Query("select sp from ServicePackage sp where sp.serviceType.serviceTypeId = :serviceTypeId "
			+ "and ((:search is not null and upper(sp.code) like CONCAT('%',upper(:search),'%')) "
			+ "or (:search is not null and upper(sp.name) like CONCAT('%',upper(:search),'%')) or "
			+ "(:search is not null and upper(sp.groupCode) like CONCAT('%',upper(:search),'%'))) order by sp.packageId desc")
	Page<ServicePackage> findAll(@Param("search") String search, @Param("serviceTypeId") Long serviceTypeId, Pageable pageable);

	@Query("select sp from ServicePackage sp where sp.serviceType.serviceTypeId = :serviceTypeId "
			+ "and upper(sp.groupCode) = upper(:groupCode) "
			+ "order by sp.packageId desc")
	Page<ServicePackage> findAllByGroupCodeAndServiceTypeId(@Param("groupCode") String groupCode, @Param("serviceTypeId") Long serviceTypeId, Pageable pageable);

	@Query("select sp from ServicePackage sp where upper(sp.groupCode) = upper(:groupCode) order by sp.packageId desc")
	Page<ServicePackage> findAllByGroupCode(@Param("groupCode") String groupCode, Pageable pageable);

	@Query("select sp from ServicePackage sp where sp.serviceType.serviceTypeId = :serviceTypeId order by sp.packageId desc")
	Page<ServicePackage> findAllByServiceTypeId(@Param("serviceTypeId") Long serviceTypeId, Pageable pageable);

	@Query("select sp from ServicePackage sp where sp.serviceType.serviceTypeId = :serviceTypeId order by sp.packageId desc")
	Page<ServicePackage> findAll(@Param("serviceTypeId") Long serviceTypeId, Pageable pageable);

	@Query("select sp from ServicePackage sp order by sp.packageId desc")
	Page<ServicePackage> findAll(Pageable pageable);

	Optional<ServicePackage> findByCode(String code);

	@Query("select sp from ServicePackage sp where sp.packageId = :packageId")
	ServicePackage findByPackageId(Long packageId);

	@Query("select sp from ServicePackage sp where sp.code = :code and sp.packageId != :packageId")
	Optional<ServicePackage> findByCodeAndPackageIdIgnore(@Param("code") String code, @Param("packageId") Long packageId);

	@Query("SELECT sp FROM ServicePackage sp WHERE sp.serviceType.serviceTypeId = :serviceTypeId ORDER BY sp.packageId desc")
	List<ServicePackage> findAllByServiceTypeId(Long serviceTypeId);

	@Query("SELECT bi FROM ServicePackage spa " +
			" INNER JOIN ServiceProgram spr ON spr.servicePackage.packageId = spa.packageId " +
			" INNER JOIN BucketsInfo bi ON bi.serviceProgram.programId = spr.programId " +
			" WHERE spr.programCode = spa.code AND spr.servicePackage.packageId = :id")
	List<BucketsInfo> findBucketsInfo(Long id);

	@Query("SELECT spa FROM ServicePackage spa " +
			" INNER JOIN ServiceProgram spr ON spr.servicePackage.packageId = spa.packageId " +
			" INNER JOIN BucketsInfo bi ON bi.serviceProgram.programId = spr.programId " +
			" WHERE spr.programCode = spa.code AND spa.packageId <> :packageId " +
			" AND ((bi.accountType = 'SUBS' AND bi.bucName = :bucName) OR (bi.accountType = 'BUNDLE' AND bi.bucType = :bucType))")
	List<ServicePackage> findBlockIN(Long packageId, String bucType, String bucName);

	@Query("SELECT spa FROM ServicePackage spa " +
			" WHERE spa.pcrfGroup.pcrfGroupId = :pcrfGroupId AND spa.packageId <> :packageId ")
	List<ServicePackage> findBlockPCRF(Long packageId, Long pcrfGroupId);

}
