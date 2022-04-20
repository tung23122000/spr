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

}
