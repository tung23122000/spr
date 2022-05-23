package dts.com.vn.repository;

import dts.com.vn.entities.MapConditionProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface MapConditionProgramRepository extends JpaRepository<MapConditionProgram, Long> {

    @Query("SELECT mcp FROM MapConditionProgram mcp WHERE mcp.programId = ?1 and mcp.conditionId.id = ?2 AND mcp.conditionValue IS NOT NULL")
    MapConditionProgram findByProgramIdAndConditionId(Long programId, Integer conditionId);

    @Query("SELECT mcp FROM MapConditionProgram mcp WHERE mcp.programId = ?1 and mcp.conditionId.id = ?2 and mcp.id " +
            "<> ?3 AND mcp.conditionValue IS NOT NULL")
    MapConditionProgram findByProgramIdAndConditionId(Long programId, Integer conditionId, Long id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value="UPDATE map_condition_program SET condition_id = ?1 WHERE id = ?2 ")
    void updateMapConditionProgram(Integer conditionId, Long id);

    @Query(nativeQuery= true, value = "SELECT * FROM map_condition_program WHERE program_id = ?1 AND is_spr = true")
    List<MapConditionProgram> findByProgramId(Long id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM map_condition_program WHERE program_id = ?1 AND condition_value " +
            "IS  NULL AND transaction = ?2 ")
    void deleteByProgramId(Long id, String transaction);

}
