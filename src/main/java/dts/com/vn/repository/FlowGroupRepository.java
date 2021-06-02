package dts.com.vn.repository;

import dts.com.vn.entities.FlowGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowGroupRepository extends JpaRepository<FlowGroup, Long> {

}
