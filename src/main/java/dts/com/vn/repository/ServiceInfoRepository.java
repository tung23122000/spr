package dts.com.vn.repository;

import dts.com.vn.entities.MinusMoney;
import dts.com.vn.entities.ServiceInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceInfoRepository extends JpaRepository<ServiceInfo, Long> {

	@Query("select si from ServiceInfo si where si.serviceProgram.programId is not null order by si.serviceInfoId desc")
	Page<ServiceInfo> findAll(Pageable pageable);

	@Query("select si from ServiceInfo si where si.serviceProgram.programId is not null "
			+ "and ((:search is not null and upper(si.infoName) like CONCAT('%',upper(:search),'%')) "
			+ "or (:search is not null and upper(si.infoValue) like CONCAT('%',upper(:search),'%')) "
			+ "or (:search is not null and upper(si.description) like CONCAT('%',upper(:search),'%')) "
			+ "or (:search is not null and upper(si.serviceProgram.description) like CONCAT('%',upper(:search),'%'))) "
			+ "order by si.serviceInfoId desc")
	Page<ServiceInfo> findAll(@Param("search") String search, Pageable pageable);

	@Query("select si from ServiceInfo si where si.serviceProgram.programId = :programId order by si.serviceInfoId desc")
	Page<ServiceInfo> findAllByProgramId(@Param("programId") Long programId, Pageable pageable);

	@Query("SELECT si FROM ServiceInfo si where si.packageId = ?1 AND si.serviceProgram.programId=?2 ORDER BY si.serviceInfoId desc")
	List<ServiceInfo> findAllByPackageId(Long packageId, Long programId);

	@Query("SELECT si FROM ServiceInfo si WHERE si.packageId = ?1 AND si.serviceProgram.programId = ?2")
	List<ServiceInfo> findByPackageIdAndProgramId(Long packageId, Long programId);

}
