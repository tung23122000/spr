package dts.com.vn.repository;

import dts.com.vn.entities.MapCommandAlias;
import dts.com.vn.entities.ServiceProgram;
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

    @Query("select mca from MapCommandAlias mca where mca.serviceProgram.programId = :programId order by mca.cmdAliasId desc")
    List<MapCommandAlias> findByProgramId(@Param("programId") Long programId);

    @Query("SELECT mca FROM MapCommandAlias mca WHERE mca.servicePackage.packageId = ?1 AND mca.serviceProgram.programId = ?2")
    List<MapCommandAlias> findByPackageIdAndProgramId(Long packageId, Long programId);

    @Query("SELECT sp FROM ServiceProgram sp " +
            " INNER JOIN MapCommandAlias mca ON mca.serviceProgram.programId = sp.programId " +
            " WHERE mca.smsMo = ?1 ")
    List<ServiceProgram> findBySmsMo(String cmdAliasName);

    @Query("SELECT sp FROM ServiceProgram sp " +
            " INNER JOIN MapCommandAlias mca ON mca.serviceProgram.programId = sp.programId " +
            " WHERE mca.smsMo = ?1 AND mca.cmdAliasId <> ?2")
    List<ServiceProgram> findBySmsMoAndCmdAliasId(String cmdAliasName, Long cmdAliasId);

    @Query("SELECT sp FROM ServiceProgram sp " +
            " INNER JOIN MapCommandAlias mca ON mca.serviceProgram.programId = sp.programId " +
            " WHERE mca.soapRequest = ?1 ")
    List<ServiceProgram> findBySoapRequest(String soapRequest);
}
