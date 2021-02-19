package dts.com.vn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import dts.com.vn.entities.NdsTypeParamProgram;

@Repository
public interface NdsTypeParamProgramRepository extends JpaRepository<NdsTypeParamProgram, Long> {

  @Query("select nds from NdsTypeParamProgram nds where nds.serviceProgram.programId = :programId order by nds.ndsTypeParamKey desc")
  Page<NdsTypeParamProgram> findAllByProgramId(@Param("programId") Long programId, Pageable pageable);
  
  @Query("select nds from NdsTypeParamProgram nds where nds.serviceProgram.programId is not null order by nds.ndsTypeParamKey desc")
  Page<NdsTypeParamProgram> findAll(Pageable pageable);
  
  @Query("select nds from NdsTypeParamProgram nds where nds.serviceProgram.programId is not null "
      + "and ((:search is not null and upper(nds.ndsType) like CONCAT('%',upper(:search),'%')) "
      + "or (:search is not null and upper(nds.ndsParam) like CONCAT('%',upper(:search),'%')) "
      + "or (:search is not null and upper(nds.ndsValue) like CONCAT('%',upper(:search),'%')) "
      + "or (:search is not null and upper(nds.servicePackage.code) like CONCAT('%',upper(:search),'%')) "
      + "or (:search is not null and upper(nds.serviceProgram.description) like CONCAT('%',upper(:search),'%'))) "
      + "order by nds.ndsTypeParamKey desc")
  Page<NdsTypeParamProgram> findAll(@Param("search") String search, Pageable pageable);
}
