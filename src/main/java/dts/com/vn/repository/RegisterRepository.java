package dts.com.vn.repository;

import dts.com.vn.entities.Register;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RegisterRepository extends JpaRepository<Register, Long> {

    @Query(nativeQuery = true, value = "SELECT COUNT (*) FROM register WHERE package_id = ?1 AND sta_datetime BETWEEN ?2 AND ?3")
    Integer findAllByPackageIdAndRegDate(Long packageId, Timestamp regDate, Timestamp endDate);

    @Query(value = "SELECT re.* FROM register re " +
            " INNER JOIN service_package sp ON sp.package_id = re.package_id " +
            " where re.ext_retry_num = ?1 and re.expire_datetime < ?2 ", nativeQuery = true)
    List<Register> getAllRenew(Long extRetryNum, Timestamp timestamp);

    @Transactional
    @Modifying
    @Query("update Register reg set reg.extRetryNum = :extRetryNum where reg.regId = :regId")
    void saveRegister(Long regId, Long extRetryNum);


    @Query(nativeQuery = true,
            value = "SELECT COUNT(DISTINCT isdn) " +
                    "FROM :patition " +
                    "WHERE ((NOW() BETWEEN sta_datetime AND end_datetime) " +
                    "OR (end_datetime IS NULL AND NOW() > sta_datetime ))")
    Long findAllPhone(String patition);

}
