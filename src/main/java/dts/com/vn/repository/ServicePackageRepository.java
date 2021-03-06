package dts.com.vn.repository;

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
//			+ "or (:search is not null and upper(sp.name) like CONCAT('%',upper(:search),'%')) or "
//			+ "(:search is not null and upper(sp.groupCode) like CONCAT('%',upper(:search),'%')) "
			+ " order by sp.systemOwner asc, sp.packageId desc")
	Page<ServicePackage> findAll(@Param("search") String search, Pageable pageable);

	@Query("select sp from ServicePackage sp where sp.serviceType.serviceTypeId = :serviceTypeId "
			+ "and (:search is not null and upper(sp.code) like CONCAT('%',upper(:search),'%')) "
//			+ "or (:search is not null and upper(sp.name) like CONCAT('%',upper(:search),'%')) or "
//			+ "(:search is not null and upper(sp.groupCode) like CONCAT('%',upper(:search),'%'))) "
			+ " order by sp.systemOwner asc, sp.packageId desc")
	Page<ServicePackage> findAll(@Param("search") String search, @Param("serviceTypeId") Long serviceTypeId, Pageable pageable);

	@Query("select sp from ServicePackage sp where sp.serviceType.serviceTypeId = :serviceTypeId order by sp.systemOwner asc, sp.packageId desc")
	Page<ServicePackage> findAll(@Param("serviceTypeId") Long serviceTypeId, Pageable pageable);

	@Query("select sp from ServicePackage sp where sp.packageId=:packageId AND sp.serviceType.serviceTypeId = :serviceTypeId order by sp.systemOwner asc, sp.packageId desc")
	Page<ServicePackage> findAll(@Param("packageId") Long packageId ,@Param("serviceTypeId") Long serviceTypeId,
								 Pageable pageable);

	@Query("select sp from ServicePackage sp where sp.packageId=:packageId order by sp.systemOwner asc, sp.packageId desc")
	Page<ServicePackage> findAllByPackageId(@Param("packageId") Long packageId , Pageable pageable);

	@Query("select sp from ServicePackage sp order by sp.systemOwner asc, sp.packageId desc")
	Page<ServicePackage> findAll(Pageable pageable);

	@Query("select sp from ServicePackage sp where sp.serviceType.serviceTypeId = :serviceTypeId "
			+ "and upper(sp.groupCode) = upper(:groupCode) "
			+ "order by sp.systemOwner asc, sp.packageId desc")
	Page<ServicePackage> findAllByGroupCodeAndServiceTypeId(@Param("groupCode") String groupCode, @Param("serviceTypeId") Long serviceTypeId, Pageable pageable);

	@Query("select sp from ServicePackage sp where upper(sp.groupCode) = upper(:groupCode) order by sp.systemOwner asc, sp.packageId desc")
	Page<ServicePackage> findAllByGroupCode(@Param("groupCode") String groupCode, Pageable pageable);

	@Query("select sp from ServicePackage sp where sp.serviceType.serviceTypeId = :serviceTypeId order by sp.systemOwner asc, sp.packageId desc")
	Page<ServicePackage> findAllByServiceTypeId(@Param("serviceTypeId") Long serviceTypeId, Pageable pageable);

	Optional<ServicePackage> findByCode(String code);

	@Query("select sp from ServicePackage sp where sp.packageId = :packageId")
	ServicePackage findByPackageId(Long packageId);

	@Query("select sp from ServicePackage sp where sp.code = :code and sp.packageId != :packageId")
	Optional<ServicePackage> findByCodeAndPackageIdIgnore(@Param("code") String code, @Param("packageId") Long packageId);

	@Query("SELECT sp FROM ServicePackage sp WHERE sp.serviceType.serviceTypeId = :serviceTypeId ORDER BY sp.systemOwner asc, sp.packageId desc")
	List<ServicePackage> findAllByServiceTypeId(Long serviceTypeId);

	@Query(nativeQuery = true,
			value = "SELECT sp.* \n" +
					"FROM service_package sp\n" +
					"WHERE sp.service_type_id = :serviceTypeId \n" +
					"  AND sp.code = :packageCode")
	ServicePackage findByServiceTypeIdAndCode(Long serviceTypeId, String packageCode);

//	@Query("SELECT bi FROM ServicePackage spa " +
//			" INNER JOIN ServiceProgram spr ON spr.servicePackage.packageId = spa.packageId " +
//			" INNER JOIN BucketsInfo bi ON bi.serviceProgram.programId = spr.programId " +
//			" WHERE spr.programCode = spa.code AND spr.servicePackage.packageId = :id")
//	List<BucketsInfo> findBucketsInfo(Long id);

	// Ch???n IN kh??ng c??ng nh??m
	@Query("SELECT DISTINCT spa FROM ServicePackage spa " +
			" INNER JOIN ServiceProgram spr ON spr.servicePackage.packageId = spa.packageId " +
			" INNER JOIN BucketsInfo bi ON bi.serviceProgram.programId = spr.programId " +
			" WHERE spa.packageId <> :packageId AND spa.serviceType.serviceTypeId <> :serviceTypeId  " +
			" AND (((bi.accountType = 'SUBS' or bi.bucType = 'SUBS') AND bi.bucName = :bucName) OR ((bi.accountType = 'BUNDLE' or bi.bucType <> 'SUBS') AND bi.bucType = :bucType))")
	List<ServicePackage> findBlockINWithoutServiceType(Long packageId, String bucType, String bucName, Long serviceTypeId);

	// Ch???n IN c??ng nh??m
	@Query("SELECT DISTINCT spa FROM ServicePackage spa " +
			" INNER JOIN ServiceProgram spr ON spr.servicePackage.packageId = spa.packageId " +
			" INNER JOIN BucketsInfo bi ON bi.serviceProgram.programId = spr.programId " +
			" WHERE spa.packageId <> :packageId AND spa.serviceType.serviceTypeId = :serviceTypeId  " +
			" AND (((bi.accountType = 'SUBS' or bi.bucType = 'SUBS') AND bi.bucName = :bucName) OR ((bi.accountType = 'BUNDLE' or bi.bucType <> 'SUBS') AND bi.bucType = :bucType))")
	List<ServicePackage> findBlockINWithServiceType(Long packageId, String bucType, String bucName, Long serviceTypeId);

	// Ch???n PCRF kh??ng c??ng nh??m
	@Query("SELECT DISTINCT spa FROM ServicePackage spa " +
			" WHERE spa.pcrfGroup like CONCAT('%',:pcrfGroupId,'%') AND spa.packageId <> :packageId AND spa.serviceType.serviceTypeId <> :serviceTypeId ")
	List<ServicePackage> findBlockPCRFWithoutServiceType(Long packageId, String pcrfGroupId, Long serviceTypeId);

	// Ch???n PCRF c??ng nh??m
	@Query("SELECT DISTINCT spa FROM ServicePackage spa " +
			" WHERE spa.pcrfGroup like CONCAT('%',:pcrfGroupId,'%') AND spa.packageId <> :packageId AND spa.serviceType.serviceTypeId = :serviceTypeId ")
	List<ServicePackage> findBlockPCRFWithServiceType(Long packageId, String pcrfGroupId, Long serviceTypeId);

	@Query(value = "SELECT sp FROM ServicePackage sp WHERE sp.systemOwner = 'FO'")
	List<ServicePackage> findAllFOPackage();

	@Query(value = "SELECT MAX(sp.packageId) FROM ServicePackage sp")
	Long findIdMax();

	@Query(value = "SELECT sp FROM ServicePackage sp WHERE sp.code LIKE '%FLEX%'")
	List<ServicePackage> findAllFlexPackage();
}
