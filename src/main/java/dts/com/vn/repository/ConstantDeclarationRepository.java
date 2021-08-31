package dts.com.vn.repository;

import dts.com.vn.entities.ConstantDeclaration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstantDeclarationRepository extends JpaRepository<ConstantDeclaration, String> {
}
