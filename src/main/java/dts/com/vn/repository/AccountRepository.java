package dts.com.vn.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dts.com.vn.entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByUsernameIgnoreCase(String username);
}
