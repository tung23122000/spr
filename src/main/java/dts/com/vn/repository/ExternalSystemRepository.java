package dts.com.vn.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import dts.com.vn.entities.ExternalSystem;

@Repository
public interface ExternalSystemRepository extends JpaRepository<ExternalSystem, Long> {

  @Query("select e from ExternalSystem e where e.status = '1'")
  List<ExternalSystem> getData();
}
