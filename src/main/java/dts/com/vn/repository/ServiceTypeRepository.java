package dts.com.vn.repository;

import dts.com.vn.entities.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {

	List<ServiceType> findByStatusAndDisplayStatus(String status, String displayStatus);
}
