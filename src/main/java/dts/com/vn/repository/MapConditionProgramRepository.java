package dts.com.vn.repository;

import dts.com.vn.entities.MapConditionProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MapConditionProgramRepository extends JpaRepository<MapConditionProgram, Long> {

    @Query("SELECT mcp FROM MapConditionProgram mcp WHERE mcp.programId = ?1 and mcp.conditionId.id = ?2")
    MapConditionProgram findByProgramIdAndConditionId(Long programId, Integer conditionId);

}
