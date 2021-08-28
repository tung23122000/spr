package dts.com.vn.repository;

import dts.com.vn.entities.BucketsInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BucketsInfoRepository extends JpaRepository<BucketsInfo, Long> {

	@Query("select b from BucketsInfo b where b.serviceProgram.programId is not null order by b.bucketsId desc")
	Page<BucketsInfo> findAll(Pageable pageable);

	@Query("select b from BucketsInfo b where b.serviceProgram.programId is not null "
			+ "and ((:search is not null and upper(b.serviceProgram.description) like CONCAT('%',upper(:search),'%')) or "
			+ "(:search is not null and upper(b.bucName) like CONCAT('%',upper(:search),'%')) or "
			+ "(:search is not null and upper(b.bucType) like CONCAT('%',upper(:search),'%'))) "
			+ "order by b.bucketsId desc")
	Page<BucketsInfo> findAll(@Param("search") String search, Pageable pageable);

	@Query("select b from BucketsInfo b where b.serviceProgram.programId is not null and b.serviceProgram.programId = :programId order by b.bucketsId desc")
	Page<BucketsInfo> findAllByProgramId(@Param("programId") Long programId, Pageable pageable);

	@Query("SELECT b FROM BucketsInfo b WHERE b.packageId = ?1 AND b.serviceProgram.programId = ?2")
	List<BucketsInfo> findByPackageIdAndProgramId(Long packageId, Long programId);

}
