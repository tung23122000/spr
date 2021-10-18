package dts.com.vn.repository;

import dts.com.vn.entities.CcspInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CcspInfoRepository extends JpaRepository<CcspInfo, Long> {
    @Query("select ci from CcspInfo ci where ci.serviceProgram.programId = :programId order by ci.ccspInfoId desc")
    Page<CcspInfo> findAllByProgramId(@Param("programId") Long programId, Pageable pageCcspInfo);

    @Query("select ci from CcspInfo ci where ci.serviceProgram.programId = :programId and ci.foName = :foName")
    CcspInfo findByProgramIdAndFoName(Long programId, String foName);


    @Query("select ci from CcspInfo ci where ci.serviceProgram.programId = :programId and ci.foName like CONCAT('%',:foResult,'%') ")
    List<CcspInfo> findByProgramIdAndFoResult(Long programId, String foResult);

    @Query("select ci from CcspInfo ci where ci.ccspValue = :ccspValue")
    CcspInfo findByCcspValue(String ccspValue);

    @Query("select ci from CcspInfo ci where ci.serviceProgram.programId = :programId and ci.foName = :foName and (ci.ccspInfoId is null or ci.ccspInfoId <> :ccspInfoId)")
    CcspInfo findByProgramIdAndFoName(Long programId, String foName, Long ccspInfoId);

    @Query("select ci from CcspInfo ci where ci.ccspValue = :ccspValue and (ci.ccspInfoId is null or ci.ccspInfoId <> :ccspInfoId)")
    CcspInfo findByCcspValue(String ccspValue, Long ccspInfoId);

}
