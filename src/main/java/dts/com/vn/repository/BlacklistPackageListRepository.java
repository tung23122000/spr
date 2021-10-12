package dts.com.vn.repository;

import dts.com.vn.entities.BlacklistPackageList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistPackageListRepository extends JpaRepository<BlacklistPackageList, Long> {
}
