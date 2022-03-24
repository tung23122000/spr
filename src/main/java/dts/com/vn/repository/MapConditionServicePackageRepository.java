package dts.com.vn.repository;

import dts.com.vn.entities.MapConditionServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapConditionServicePackageRepository extends JpaRepository<MapConditionServicePackage, Long> {

	@Query("SELECT map FROM MapConditionServicePackage map WHERE map.packageId = ?1 and map.programId = ?2 ORDER BY map.condition.conditionId DESC")
	List<MapConditionServicePackage> getCondition(Long packageId, Long programId);

}
