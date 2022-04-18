package dts.com.vn.repository;

import dts.com.vn.entities.NeifInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Repository
public interface NeifInfoRepository extends JpaRepository<NeifInfo, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM neif_info WHERE isdn = ?1")
    NeifInfo findDataByIsdn(String isdn);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE neif_info " +
            "SET bonus_amount = ?1,insert_date = ?2,main_amount = ?3,neif_message = ?4, profile = ?5, reg_date = ?6,status=?7 " +
            "WHERE isdn = ?8")
    void updateNeifInfo(String bonusAmount, Timestamp insertDate, String mainAmount, String neifMessage,
                        String profile, Timestamp regDate, String status, String isdn);

}
