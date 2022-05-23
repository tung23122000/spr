package dts.com.vn.repository;

import dts.com.vn.entities.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Long> {

    @Query(nativeQuery = true, value ="SELECT * FROM reports WHERE report_type = 1 AND report_date = ?1")
    Reports getReport1(Timestamp date);

    @Query(nativeQuery = true, value ="SELECT * FROM reports WHERE report_type = 2 AND report_date = ?1")
    Reports getReport2(Timestamp date);

    @Query(nativeQuery = true, value ="SELECT * FROM reports WHERE report_type = 3 AND report_date = ?1")
    Reports getReport3(Timestamp date);

    @Query(nativeQuery = true, value ="SELECT * FROM reports WHERE report_type = 4 AND report_date = ?1")
    Reports getReport4(Timestamp date);

    @Query(nativeQuery = true, value ="SELECT * FROM reports WHERE report_type = 5 AND report_date = ?1")
    Reports getReport5(Timestamp date);

}
