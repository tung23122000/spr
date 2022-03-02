package dts.com.vn.repository;

import dts.com.vn.entities.PCRFGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PCRFGroupRepository extends JpaRepository<PCRFGroup, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM pcrf_group WHERE pcrf_group_id = :id")
    PCRFGroup findPCRFGroupById(Long id);

}
