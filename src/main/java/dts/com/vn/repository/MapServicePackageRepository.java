package dts.com.vn.repository;

import dts.com.vn.entities.MapServicePackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapServicePackageRepository extends JpaRepository<MapServicePackage, Long> {

	@Query("select m from MapServicePackage m where m.serviceProgram.programId is not null order by m.mapId desc")
	Page<MapServicePackage> findAll(Pageable pageable);

	@Query("select m from MapServicePackage m where m.serviceProgram.programId is not null "
			+ "and ((:search is not null and upper(m.regMapCode) like CONCAT('%',upper(:search),'%')) or "
			+ "(:search is not null and upper(m.delMapCode) like CONCAT('%',upper(:search),'%')) "
			+ "or (:search is not null and upper(m.promCode) like CONCAT('%',upper(:search),'%')) "
			+ "or (:search is not null and upper(m.mobType) like CONCAT('%',upper(:search),'%')) "
			+ "or (:search is not null and upper(m.serviceProgram.description) like CONCAT('%',upper(:search),'%'))) "
			+ "order by m.mapId desc")
	Page<MapServicePackage> findAll(@Param("search") String search, Pageable pageable);

	@Query("select m from MapServicePackage m where m.serviceProgram.programId = :programId order by m.mapId desc")
	Page<MapServicePackage> findAllByProgramId(@Param("programId") Long programId, Pageable pageable);

	@Query("SELECT msp FROM MapServicePackage msp WHERE msp.packageId = ?1 AND msp.serviceProgram.programId = ?2")
	List<MapServicePackage> getListClone(Long packageId, Long programId);

}
