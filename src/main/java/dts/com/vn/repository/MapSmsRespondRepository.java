package dts.com.vn.repository;

import dts.com.vn.entities.MapSmsRespond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MapSmsRespondRepository extends JpaRepository<MapSmsRespond, Long> {
    @Query("SELECT msr FROM MapSmsRespond msr WHERE msr.serviceProgram.programId = ?1")
    List<MapSmsRespond> findAllByProgramId(Long programId);

    @Query("SELECT msr FROM MapSmsRespond msr WHERE msr.smsRespond = ?1")
    MapSmsRespond findBySmsRespond(String smsRespond);

    @Query("SELECT msr FROM MapSmsRespond msr WHERE msr.smsRespond = ?1 AND  msr.mapSmsRespondId <> ?2")
    MapSmsRespond findBySmsRespond(String smsRespond, Long mapSmsRespondId);

}
