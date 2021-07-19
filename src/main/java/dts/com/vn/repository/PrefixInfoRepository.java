package dts.com.vn.repository;

import dts.com.vn.entities.PrefixInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrefixInfoRepository extends JpaRepository<PrefixInfo, Long> {
}
