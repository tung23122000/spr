package dts.com.vn.repository;

import dts.com.vn.entities.PCRFGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PCRFGroupRepository extends JpaRepository<PCRFGroup, Long> {
}
