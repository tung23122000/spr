package dts.com.vn.repository;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import dts.com.vn.entities.ServicePackage;

@Repository
public interface ServicePackageRepository extends JpaRepository<ServicePackage, Long> {

  @Query("select sp from ServicePackage sp where (:search is not null and sp.code like CONCAT('%',:search,'%')) "
      + "or (:search is not null and sp.name like CONCAT('%',:search,'%')) or "
      + " (:search is not null and sp.groupCode like CONCAT('%',:search,'%')) order by sp.packageId desc")
  Page<ServicePackage> findAll(@Param("search") String search, Pageable pageable);
  
  @Query("select sp from ServicePackage sp order by sp.packageId desc")
  Page<ServicePackage> findAll(Pageable pageable);

  Optional<ServicePackage> findByCode(String code);
}
