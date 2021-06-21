package dts.com.vn.repository;

import dts.com.vn.entities.MapCommandAlias;
import dts.com.vn.entities.NdsTypeParamProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MapCommandAliasRepository extends JpaRepository<MapCommandAlias, Long> {
    @Query("select mca from MapCommandAlias mca where mca.serviceProgram.programId = :programId order by mca.mapCommandAliasId desc")
    Page<MapCommandAlias> findAllByProgramId(@Param("programId") Long programId, Pageable pageable);
}
