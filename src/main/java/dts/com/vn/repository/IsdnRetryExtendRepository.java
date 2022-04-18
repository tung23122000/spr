package dts.com.vn.repository;

import dts.com.vn.entities.IsdnRetryExtend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface IsdnRetryExtendRepository extends JpaRepository<IsdnRetryExtend, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM isdn_retry_extend WHERE isdn = ?1")
    IsdnRetryExtend findDataFromIsdnRetryExtendByIsdn(String isdn);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE isdn_retry_extend " +
            "SET status = '2' " +
            "WHERE isdn = ?1 " +
            "AND insert_date BETWEEN (NOW()-INTERVAL '30 DAY') AND NOW()")
    void updateNeifInfo(String isdn);

}
