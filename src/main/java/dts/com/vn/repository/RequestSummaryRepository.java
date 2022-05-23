package dts.com.vn.repository;

import dts.com.vn.entities.RequestSummary;
import dts.com.vn.response.Report3FromDbResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface RequestSummaryRepository extends JpaRepository<RequestSummary, Integer> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM request_summary ORDER BY response_date DESC LIMIT 1")
    RequestSummary findLastRequestSummary();

    @Query(nativeQuery = true, value = "SELECT COUNT(request_command), request_command as requestcommand,chanel as channel,isdn " +
            "FROM request_summary " +
            "WHERE received_date BETWEEN ?1 AND ?2\n" +
            "GROUP BY request_command,chanel,isdn " +
            "ORDER BY count DESC " +
            "LIMIT 100 ")
    List<Report3FromDbResponse> getDataFromDbByIsdnAndDate(Timestamp startDate, Timestamp endDate);
}
