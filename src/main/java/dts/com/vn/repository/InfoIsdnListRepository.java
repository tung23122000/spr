package dts.com.vn.repository;

import dts.com.vn.entities.InfoIsdnList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface InfoIsdnListRepository extends JpaRepository<InfoIsdnList, Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value = " UPDATE info_isdn_list set name_isdn_list = ?1, name_target_folder = ?2, " +
            "name_target_system = ?3, user_name = ?4, pass_word = ?5, isdn_list_id = ?7 " +
            "WHERE id = ?6 ")
    void updateInfoIsdnList(String nameIdsnList ,String nameTargetFolder, String nameTargetSystem,String user,
                            String password, Long id,
                            Long isdnListId);

}
