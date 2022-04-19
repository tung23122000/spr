package dts.com.vn.repository;

import dts.com.vn.entities.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Long> {

    @Query(nativeQuery = true, value ="SELECT * FROM report_part1 WHERE report_date = ?1")
    Reports getDataByDate(Timestamp date);

}
