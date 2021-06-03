package dts.com.vn.repository;

import dts.com.vn.entities.ListDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListDetailRepository extends JpaRepository<ListDetail, Long> {

}
