package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sub_package_info", schema = "public")
public class SubPackageInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_package_info_id")
    private Long subPackageInfoId;

    @Column(name = "package_id")
    private Long packageId;

    @Column(name = "package_code")
    private String packageCode;

    @Column(name = "flex_sub_package_id")
    private Long flexSubPackageId;

}
