package dts.com.vn.repository;

import dts.com.vn.entities.Register;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {

	@Query(value = "SELECT COUNT (*) FROM register WHERE package_id = ?1 AND sta_datetime BETWEEN ?2 AND ?3", nativeQuery = true)
	Integer findAllByPackageIdAndRegDate(Long packageId, Timestamp regDate, Timestamp endDate);

	@Query(value = "SELECT re.* FROM register re " +
			" INNER JOIN service_package sp ON sp.package_id = re.package_id " +
			" where re.ext_retry_num = ?1 and re.expire_datetime < ?2 ", nativeQuery = true)
	List<Register> getAllRenew(Long extRetryNum, Timestamp timestamp);

	@Transactional
	@Modifying
	@Query("update Register reg set reg.extRetryNum = :extRetryNum where reg.regId = :regId")
	void saveRegister(Long regId, Long extRetryNum);
}
