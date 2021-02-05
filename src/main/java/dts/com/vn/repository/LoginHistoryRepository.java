package dts.com.vn.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dts.com.vn.entities.LoginHistory;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

  Optional<LoginHistory> findByTokenKey(String tokenKey);
}
