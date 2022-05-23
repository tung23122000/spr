package dts.com.vn.repository;

import dts.com.vn.entities.ListCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ListConditionRepository extends JpaRepository<ListCondition, Integer> {

    @Query(nativeQuery = true,value = "SELECT MAX( ID ) FROM list_condition")
    Integer maxId();

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value="UPDATE list_condition SET status = false WHERE id= ?1")
    void deleteConditionById(Integer id);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value="UPDATE list_condition SET is_package = ?2 WHERE id= ?1")
    void updateIsPackgeById(Integer id, Boolean isPackage);

    @Query(nativeQuery = true,value = "SELECT * FROM list_condition  WHERE condition_name = ?1")
    ListCondition findConditionIdByName(String conditionName);

}
