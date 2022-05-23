package dts.com.vn.repository;

import dts.com.vn.entities.ServicePackageList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Repository
public interface ServicePackageListRepository extends JpaRepository<ServicePackageList, Long> {

	void deleteByIsdnListId(Long isdnListId);

	@Transactional
	@Modifying
	@Query(nativeQuery =true,value ="UPDATE service_package_list \n" +
			"SET sta_date = ?2, end_date = ?3 \n" +
			"WHERE isdn_list_id = ?1 ")
	void updateWhiteListByIsdnListId(Long isdnListId, Instant staDate, Instant endDate);

	@Transactional
	@Modifying
	@Query(nativeQuery =true,value ="UPDATE service_package_list \n" +
			"SET sta_date = :staDate, end_date = NUll " +
			"WHERE isdn_list_id = :isdnListId")
	void updateWhiteListByIsdnListIdByStaDate(Long isdnListId, Instant staDate);

}
