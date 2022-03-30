package dts.com.vn.repository;

import dts.com.vn.entities.BlacklistPackageList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Repository
public interface BlacklistPackageListRepository extends JpaRepository<BlacklistPackageList, Long> {

	void deleteByIsdnListId(Long listId);

	@Transactional
	@Modifying
	@Query(nativeQuery =true,value ="UPDATE blacklist_package_list \n" +
			"SET sta_date = :staDate, end_date = null \n" +
			"WHERE isdn_list_id = :isdnListId")
	void updateBlackListByIsdnListIdAndStaDate(Long isdnListId, Instant staDate);

	@Transactional
	@Modifying
	@Query(nativeQuery =true,value ="UPDATE blacklist_package_list \n" +
			"SET sta_date = :staDate, end_date = :endDate \n" +
			"WHERE isdn_list_id = :isdnListId")
	void updateBlackListByIsdnListId(Long isdnListId, Instant staDate, Instant endDate);

}
