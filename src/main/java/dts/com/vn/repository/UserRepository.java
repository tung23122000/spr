package dts.com.vn.repository;

import dts.com.vn.entities.Account;
import dts.com.vn.entities.MapRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Account, Long> {
    @Query("SELECT acc FROM Account acc WHERE  acc.username = :username")
    Account getAccountByUsername(@Param("username") String username);
}
