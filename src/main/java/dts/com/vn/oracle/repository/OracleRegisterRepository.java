package dts.com.vn.oracle.repository;

import dts.com.vn.oracle.entities.OracleRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OracleRegisterRepository extends JpaRepository<OracleRegister, Long> {
}
