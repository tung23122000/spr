package dts.com.vn.repository;

import dts.com.vn.entities.ServiceProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProgramRepository extends JpaRepository<ServiceProgram, Long> {

	@Query("select sp from ServiceProgram sp where (:search is not null and upper(sp.servicePackage.code) like CONCAT('%',upper(:search),'%')) "
			+ "or (:search is not null and upper(sp.description) like CONCAT('%',upper(:search),'%')) "
			+ " order by sp.programId desc")
	Page<ServiceProgram> findAll(String search, Pageable pageable);

	@Query("select sp from ServiceProgram sp order by sp.programId desc")
	Page<ServiceProgram> findAll(Pageable pageable);

	@Query("select sp from ServiceProgram sp where sp.servicePackage.packageId = :packageId order by sp.programId desc")
	Page<ServiceProgram> findByPackageId(@Param("packageId") Long packageId, Pageable pageable);

	@Query("select sp from ServiceProgram sp where sp.servicePackage.packageId = :packageId order by sp.programId desc")
	List<ServiceProgram> findByPackageIdWithoutPageable(@Param("packageId") Long packageId);

	@Query("SELECT sp FROM ServiceProgram sp WHERE sp.servicePackage.packageId = ?1")
	List<ServiceProgram> findAllByPackageId(Long packageId);

	@Query("SELECT sp FROM ServiceProgram sp WHERE sp.programCode = ?1")
	List<ServiceProgram> findByProgramCode(String programCode);

	@Query("SELECT sp FROM ServiceProgram sp WHERE sp.programId <> ?1 AND sp.programCode = ?2")
	List<ServiceProgram> findByProgramIdAndProgramCode(Long programId, String programCode);

	@Query("SELECT sp FROM ServiceProgram sp WHERE sp.servicePackage.packageId = ?1 AND sp.isDefaultProgram = true")
	List<ServiceProgram> findDefaultProgram(Long packageId);

	@Query(nativeQuery = true,value = "SELECT * FROM service_program WHERE program_id= ?1")
	ServiceProgram findByProgramId(Long id);

	@Query(nativeQuery = true,value = "SELECT program_id FROM service_program  " +
			"WHERE program_code = ?1 AND package_id = ?2 ")
	Long findProgramIdByCode(String programCode, Long packageId );

	@Query(nativeQuery = true, value="SELECT*FROM service_program WHERE package_id = ?1 " +
			"AND ((sta_date < NOW() AND end_date > NOW()) OR (sta_date < NOW() AND end_date IS NULL))")
	List<ServiceProgram> findAllActiveByPackageId(Long packageId);

	@Query("SELECT sp.programId FROM ServiceProgram sp WHERE sp.servicePackage.packageId = ?1")
	List<Long> findAllProgramId(Long packageId);

}
