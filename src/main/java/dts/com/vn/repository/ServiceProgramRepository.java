package dts.com.vn.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import dts.com.vn.entities.ServiceProgram;

@Repository
public interface ServiceProgramRepository extends JpaRepository<ServiceProgram, Long> {

  @Query("select sp from ServiceProgram sp where (:search is not null and upper(sp.servicePackage.code) like CONCAT('%',upper(:search),'%')) "
      + "or (:search is not null and upper(sp.description) like CONCAT('%',upper(:search),'%')) "
      + " order by sp.programId desc")
  Page<ServiceProgram> findAll(String search, Pageable pageable);
  
  @Query("select sp from ServiceProgram sp order by sp.programId desc")
  Page<ServiceProgram> findAll(Pageable pageable);
  
  @Query("select sp from ServiceProgram sp where sp.servicePackage.packageId = :packageId order by sp.programId desc")
  Page<ServiceProgram> findByPackageId(@Param("packageId") Long packageId, Pageable pageable);
}
