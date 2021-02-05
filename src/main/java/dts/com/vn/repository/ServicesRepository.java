package dts.com.vn.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dts.com.vn.entities.Services;

@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {

  List<Services> findAll();
}
