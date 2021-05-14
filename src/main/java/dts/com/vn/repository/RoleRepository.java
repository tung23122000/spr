package dts.com.vn.repository;

import dts.com.vn.entities.MapRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<MapRolePermission, Long> {
	@Query("SELECT map FROM MapRolePermission map WHERE  map.role.id = :id")
	List<MapRolePermission> getPermissionById(@Param("id") Long id);
}
