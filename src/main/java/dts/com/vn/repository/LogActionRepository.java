package dts.com.vn.repository;

import dts.com.vn.entities.LogAction;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.request.LogActionRequest;
import java.sql.Timestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogActionRepository extends JpaRepository<LogAction, Long> {

    @Query("select la from LogAction la where (la.action is null or la.action = :action) and (la.timeAction between :startDate and :endDate) order by la.timeAction desc")
    Page<LogAction> findAll(String action, Timestamp startDate, Timestamp endDate, Pageable pageable);
}
