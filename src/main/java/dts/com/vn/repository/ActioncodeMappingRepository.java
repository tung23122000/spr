package dts.com.vn.repository;

import dts.com.vn.entities.ActioncodeMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActioncodeMappingRepository extends JpaRepository<ActioncodeMapping, Long> {
}
