package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "pcrf_group", schema = "public")
public class PCRFGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pcrf_group_id")
    private Long pcrfGroupId;

    @Column(name = "pcrf_group_name")
    private String pcrfGroupName;

    @Column(name = "pcrf_group_description")
    private String pcrfGroupDescription;

    @Column(name = "pcrf_group_code")
    private String pcrfGroupCode;
}
