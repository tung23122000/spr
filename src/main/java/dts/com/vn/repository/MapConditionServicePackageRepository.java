package dts.com.vn.repository;

import dts.com.vn.entities.MapConditionServicePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MapConditionServicePackageRepository extends JpaRepository<MapConditionServicePackage, Long> {
    @Query("select map from MapConditionServicePackage map where map.packageId = :packageId and map.programId = :programId order by map.condition.conditionId asc")
    List<MapConditionServicePackage> getCondition(@Param("packageId") Long packageId, @Param("programId") Long programId);
}
