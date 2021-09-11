package dts.com.vn.repository;

import dts.com.vn.entities.AutoExtendPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoExtendPackageRepository extends JpaRepository<AutoExtendPackage, Long> {

}
