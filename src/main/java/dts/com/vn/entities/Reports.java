package dts.com.vn.entities;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Table(name = "reports", schema = "public")
@Entity
public class Reports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="report_id")
    private Long reportId;

    @Column(name= "report_type")
    private Integer reportType;

    @Column(name="report_date")
    private Timestamp reportDate;

    @Column(name="insert_at")
    private Timestamp insertAt;

    @Column(name="report_data")
    @Type(type = "jsonb")
    private Object reportData;

}
