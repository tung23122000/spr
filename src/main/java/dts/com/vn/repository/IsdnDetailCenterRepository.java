package dts.com.vn.repository;

import dts.com.vn.entities.IsdnDetailCenter;
import dts.com.vn.entities.ServicePackage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IsdnDetailCenterRepository extends JpaRepository<IsdnDetailCenter, Long> {

    @Query("select idc from IsdnDetailCenter idc order by idc.isdnDetailCenterId desc")
    Page<IsdnDetailCenter> findAll(Pageable pageable);

    @Query("select idc from IsdnDetailCenter idc " +
            " where :search is not null " +
            " and ((upper(idc.isdnPrefix) like CONCAT('%',upper(:search),'%')) " +
            " or (upper(idc.network) like CONCAT('%',upper(:search),'%')) " +
            " or (upper(idc.centerId) like CONCAT('%',upper(:search),'%')))" +
            " order by idc.isdnDetailCenterId desc")
    Page<IsdnDetailCenter> findAll(String search, Pageable pageable);

}
