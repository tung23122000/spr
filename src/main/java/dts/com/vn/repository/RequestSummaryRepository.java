package dts.com.vn.repository;

import dts.com.vn.entities.RequestSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestSummaryRepository extends JpaRepository<RequestSummary, Integer> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM request_summary ORDER BY response_date DESC LIMIT 1")
    RequestSummary findLastRequestSummary();

}
