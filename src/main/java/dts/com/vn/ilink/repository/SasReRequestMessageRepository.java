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
                    "WHERE finished_time > ?1 AND finished_time < ?2 " +
                    "AND req_status IN (7,8,9) " +
                    "ORDER BY request_id ASC")
    List<SasReRequestMessage> findAllBeetweenDate(Timestamp startDate, Timestamp endDate);

}
