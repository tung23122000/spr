package dts.com.vn.repository;

import dts.com.vn.entities.IsdnList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IsdnListRepository  extends JpaRepository<IsdnList, Long> {
    @Query("select il from IsdnList il " +
            " order by il.isdnListId desc")
    Page<IsdnList> findAll(Pageable pageable);

    @Query("select il from IsdnList il " +
            " left join ServicePackageList spl on spl.isdnListId = il.isdnListId " +
            " left join BlacklistPackageList bpl on bpl.isdnListId = il.isdnListId " +
            " where spl.isdnListId is null and bpl.isdnListId is null ")
    List<IsdnList> findAllNotMapped();

    @Query("select il from IsdnList il " +
            " left join ServicePackageList spl on spl.isdnListId = il.isdnListId " +
            " where spl.programId = :programId ")
    List<IsdnList> getWhiteListByProgramId(Long programId);

    @Query("select il from IsdnList il " +
            " left join BlacklistPackageList bpl on bpl.isdnListId = il.isdnListId " +
            " where bpl.programId = :programId ")
    List<IsdnList> getBlackListByProgramId(Long programId);

    @Query(nativeQuery = true, value = "SELECT * FROM isdn_list WHERE isdn_list_id = ?1 " +
            "AND (end_date > NOW() OR end_date IS NULL)")
    IsdnList findByIsdnListId(Long isdnListId);
}
