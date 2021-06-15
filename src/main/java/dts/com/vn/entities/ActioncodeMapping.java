package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "actioncode_mapping", schema = "public")
public class ActioncodeMapping {
    @Id
    @Column(name = "ac_key")
    private String acKey;

    @Column(name = "ac_value")
    private String acValue;
}
