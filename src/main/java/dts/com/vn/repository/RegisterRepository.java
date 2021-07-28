package dts.com.vn.repository;

import dts.com.vn.entities.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {

	@Query(value = "SELECT COUNT (*) FROM register WHERE package_id = ?1 AND sta_datetime BETWEEN ?2 AND ?3", nativeQuery = true)
	Integer findAllByPackageIdAndRegDate(Long packageId, Timestamp regDate, Timestamp endDate);

}
