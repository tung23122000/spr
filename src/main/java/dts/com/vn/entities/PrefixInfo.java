package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "prefix_info", schema = "public")
public class PrefixInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prefix_info_id")
    private Long prefixInfoId;

    @Column(name = "prefix_info_name")
    private String prefixInfoName;

    @Column(name = "prefix_info_status")
    private String prefixInfoStatus;

    @Column(name = "prefix_info_subpartition")
    private String prefixInfoSubpartition;
}
