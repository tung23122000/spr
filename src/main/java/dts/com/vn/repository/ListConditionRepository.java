package dts.com.vn.repository;

import dts.com.vn.entities.ListCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListConditionRepository extends JpaRepository<ListCondition, Integer> {
}
