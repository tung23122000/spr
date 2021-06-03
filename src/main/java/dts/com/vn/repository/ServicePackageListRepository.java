package dts.com.vn.repository;

import dts.com.vn.entities.ServicePackageList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServicePackageListRepository extends JpaRepository<ServicePackageList, Long> {

}
