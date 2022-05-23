package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sub_service_program", schema = "public")
public class SubServiceProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_service_program_id")
    private Long subServiceProgramId;

    @Column(name = "package_id")
    private Long packageId;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "max_pcrf_service_exclude")
    private String maxPcrfServiceExclude;

    @Column(name = "max_package_exclude")
    private String maxPackageExclude;

    @Column(name = "max_package_group_exclude")
    private String maxPackageGroupExclude;

    @Column(name = "flex_sub_program_id")
    private Long flexSubProgramId;

    @Column(name = "flex_filter_bundle")
    private String flexFilterBundle;

    @Column(name = "flex_min_qty")
    private String flexMinQty;

}
