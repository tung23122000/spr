package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "flow_group", schema = "public")
public class FlowGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "flow_group_id")
    private Long flowGroupId;

    @Column(name = "flow_group_name")
    private String flowGroupName;
}
