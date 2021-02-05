package dts.com.vn.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dts.com.vn.entities.ServiceType;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {

  List<ServiceType> findByStatusAndDisplayStatus(String status, String displayStatus);
}
