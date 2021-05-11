package dts.com.vn.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import dts.com.vn.entities.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

  Optional<Account> findByUsernameIgnoreCase(String username);

  @Query("SELECT acc FROM Account acc WHERE  acc.username = :username")
  Account getAccountByUsername(@Param("username") String username);
}
