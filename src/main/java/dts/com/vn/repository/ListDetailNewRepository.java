package dts.com.vn.repository;

import dts.com.vn.entities.ListDetailNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ListDetailNewRepository extends JpaRepository<ListDetailNew, Integer> {

	void deleteByIsdnListId(Long listId);

	@Transactional
	@Modifying
	@Query(nativeQuery=true,value = "UPDATE list_detail_new \n" +
			"SET status = ?2 \n" +
			"WHERE isdn_list_id = ?1")
	void updateListDetailNewStatus(Long isdnListId,String status);

}
