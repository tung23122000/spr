package dts.com.vn.repository;

import dts.com.vn.entities.ExternalSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExternalSystemRepository extends JpaRepository<ExternalSystem, Long> {

	@Query("select e from ExternalSystem e where e.status = '1'")
	List<ExternalSystem> getData();
}
