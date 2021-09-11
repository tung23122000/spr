package dts.com.vn.repository;

import dts.com.vn.entities.MapCommandAlias;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapCommandAliasRepository extends JpaRepository<MapCommandAlias, Long> {
    @Query("select mca from MapCommandAlias mca where mca.serviceProgram.programId = :programId order by mca.cmdAliasId desc")
    Page<MapCommandAlias> findAllByProgramId(@Param("programId") Long programId, Pageable pageable);

    @Query("SELECT mca FROM MapCommandAlias mca WHERE mca.servicePackage.packageId = ?1 AND mca.serviceProgram.programId = ?2")
    List<MapCommandAlias> findByPackageIdAndProgramId(Long packageId, Long programId);

    @Query("SELECT mca FROM MapCommandAlias mca WHERE mca.cmdAliasName = ?1 ")
    List<MapCommandAlias> findByCmdAliasName(String cmdAliasName);

    @Query("SELECT mca FROM MapCommandAlias mca WHERE mca.cmdAliasName = ?1 AND mca.cmdAliasId <> ?2")
    List<MapCommandAlias> findByCmdAliasNameAndCmdAliasId(String cmdAliasName, Long cmdAliasId);
}
