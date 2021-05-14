package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "cp_sub_act_status", schema = "public")
public class CpSubActStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sub_id")
	private Long subId;

	@Column(name = "isdn")
	private String isdn;

	@Column(name = "act_status")
	private String actStatus;

	@Column(name = "update_time")
	private Instant updateTime;
}
