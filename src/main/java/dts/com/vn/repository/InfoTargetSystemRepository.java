package dts.com.vn.repository;

import dts.com.vn.entities.InfoTargetSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InfoTargetSystemRepository extends JpaRepository<InfoTargetSystem, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM info_target_system WHERE name_target_system LIKE ?1")
    InfoTargetSystem findByNameTargetSystem(String name);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = "UPDATE info_target_system SET name_target_system = ?1,  ip_target_system= ?2" +
            " ,port_target_system = ?3 " +
            "WHERE id = ?4 ")
    void updateInfoTargetSystem(String nameTargetSystem, String ipTargetSystem, Integer portTargetSystem, Long id);


}
