package dts.com.vn.ilink.repository;

import dts.com.vn.ilink.entities.SasReRequestMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SasReRequestMessageRepository extends JpaRepository<SasReRequestMessage, Long> {

    @Query(nativeQuery = true,
            value = "SELECT *  FROM ilink.sas_re_request_message " +
                    "WHERE received_time > ?1 AND received_time < ?2 " +
                    "ORDER BY request_id ASC")
    List<SasReRequestMessage> findAllBeetweenDate(Timestamp startDate, Timestamp endDate);

}
