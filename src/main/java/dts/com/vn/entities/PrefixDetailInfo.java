package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "prefix_detail_info", schema = "public")
public class PrefixDetailInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prefix_detail_id")
    private Long prefixDetailId;

    @Column(name = "prefix_info_id")
    private Long prefixInfoId;

    @Column(name = "prefix_detail_isdn")
    private String prefixDetailIsdn;

    @Column(name = "prefix_detail_status")
    private String prefixDetailStatus;
}
