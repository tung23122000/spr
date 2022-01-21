package dts.com.vn.repository;

import dts.com.vn.entities.ConfigFlowCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigFlowConditionRepository extends JpaRepository<ConfigFlowCondition, Long> {

}
