package dts.com.vn.repository;

import dts.com.vn.entities.IsdnList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IsdnListRepository  extends JpaRepository<IsdnList, Long> {
    @Query("select il from IsdnList il " +
            " order by il.isdnListId desc")
    Page<IsdnList> findAll(Pageable pageable);
}
