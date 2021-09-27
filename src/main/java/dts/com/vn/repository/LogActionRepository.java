package dts.com.vn.repository;

import dts.com.vn.entities.LogAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface LogActionRepository extends JpaRepository<LogAction, Long> {

    @Query("select la from LogAction la where (:action is null or la.action = :action) " +
            " and (cast(:startDate as date) is null  or la.timeAction >= :startDate) " +
            " and (cast(:endDate as date) is null  or la.timeAction <= :endDate) " +
            " order by la.logActionId desc")
    Page<LogAction> findAll(String action, Timestamp startDate, Timestamp endDate, Pageable pageable);
}
