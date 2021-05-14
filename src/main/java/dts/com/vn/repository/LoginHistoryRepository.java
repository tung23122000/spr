package dts.com.vn.repository;

import dts.com.vn.entities.LoginHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {

	Optional<LoginHistory> findByTokenKey(String tokenKey);
}
