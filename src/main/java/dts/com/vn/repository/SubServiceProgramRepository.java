package dts.com.vn.repository;

import dts.com.vn.entities.SubServiceProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SubServiceProgramRepository extends JpaRepository<SubServiceProgram, Long> {

    @Query(nativeQuery = true, value= "SELECT * FROM sub_service_program WHERE program_id = ?1")
    SubServiceProgram findByProgramId(Long programId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value= "UPDATE sub_service_program " +
            "SET max_pcrf_service_exclude= ?2 , max_package_exclude = ?3, max_package_group_exclude = ?4" +
            "WHERE program_id = ?1")
    void updateMaxByProgramId(Long programId, String maxPcrfServiceExclude, String maxPackageExclude,
                            String maxPackageGroupExclude);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value= "UPDATE sub_service_program " +
            "SET flex_sub_program_id = ?2 , flex_filter_bundle = ?3, flex_min_qty = ?4 " +
            "WHERE program_id = ?1")
    void updateFlexByProgramId(Long programId, Long flexSubProgramId, String flexFilterBundle,
                              String flexMinQty);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value= "UPDATE sub_service_program " +
            "SET is_dk_retry = ?2 , is_cancel = ?3, is_insert = ?4 " +
            "WHERE program_id = ?1")
    void updateByProgramId(Long programId, Boolean isDkRetry, Boolean isCancel, Boolean isInsert);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value= "UPDATE sub_service_program " +
            "SET register_number_day = ?2, renew_number_day = ?3 " +
            "WHERE program_id = ?1")
    void updateWithProgramId(Long programId, Long registerNumberDay, Long renewNumberDay);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value= "UPDATE sub_service_program " +
            "SET sale_charge_price = ?2 " +
            "WHERE program_id = ?1")
    void updateSaleCharge(Long programId, String saleChargePrice);

    @Modifying
    @Transactional
    @Query(nativeQuery = true, value= "DELETE FROM sub_service_program WHERE program_id = ?1")
    void deleteByProgramId(Long programId);

}
