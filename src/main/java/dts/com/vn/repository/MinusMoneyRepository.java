package dts.com.vn.repository;

import dts.com.vn.entities.MinusMoney;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MinusMoneyRepository extends JpaRepository<MinusMoney, Long> {
    @Query("SELECT mn FROM MinusMoney mn WHERE  mn.servicePackage.packageId = :packageId AND mn.serviceProgram.programId = :programId ORDER BY mn.minusMoneyLadderId ASC")
    List<MinusMoney> getAll(Long packageId, Long programId);


    @Query("SELECT mn FROM MinusMoney mn WHERE mn.serviceProgram.programId = :programId ORDER BY mn.minusMoneyLadderId ASC")
    List<MinusMoney> findAllByProgramId(Long programId);

    @Query("SELECT mn FROM MinusMoney mn WHERE mn.servicePackage.packageId = ?1 AND mn.serviceProgram.programId = ?2")
    List<MinusMoney> findByPackageIdAndProgramId(Long packageId, Long programId);
}
