package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "isdn_ctkm", schema = "public")
public class IsdnCtkm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "isdn")
    private String isdn;

    @Column(name = "reg_time")
    private Timestamp regTime;

    @Column(name = "status")
    private String status;

}
