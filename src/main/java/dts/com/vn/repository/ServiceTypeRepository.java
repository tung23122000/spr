package dts.com.vn.repository;

import dts.com.vn.entities.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceTypeRepository extends JpaRepository<ServiceType, Long> {

	List<ServiceType> findByStatusAndDisplayStatus(String status, String displayStatus);

	@Query(nativeQuery= true,value = "SELECT service_type_id FROM service_type ORDER BY service_type_id ASC")
	List<Long> findListServiceTypeId();

}
