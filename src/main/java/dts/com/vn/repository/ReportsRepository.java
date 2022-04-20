package dts.com.vn.repository;

import dts.com.vn.entities.Reports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface ReportsRepository extends JpaRepository<Reports, Long> {

    @Query(nativeQuery = true, value ="SELECT * FROM reports WHERE report_type = 1 AND report_date = ?1")
    Reports getDailyReportByDate(Timestamp date);

    @Query(nativeQuery = true, value ="SELECT * FROM reports WHERE report_type = 2 AND report_date = ?1")
    Reports getReportRetryRenewPackage(Timestamp date);

    @Query(nativeQuery = true, value ="SELECT * FROM reports WHERE report_type = 3 AND report_date = ?1")
    Reports getReportTopIsdnByDate(Timestamp date);

    @Query(nativeQuery = true, value ="SELECT * FROM reports WHERE report_type = 4 AND report_date = ?1")
    Reports getReportRenewFailed(Timestamp date);

    @Query(nativeQuery = true, value ="SELECT * FROM reports WHERE report_type = 5 AND report_date = ?1")
    Reports getReportDataSystem(Timestamp date);

}
