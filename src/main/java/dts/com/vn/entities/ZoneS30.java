package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "zone_s30", schema = "public")
public class ZoneS30 {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "zone_id")
	private Long zoneId;

	@Column(name = "zone_name")
	private String zoneName;

	@Column(name = "zone_in")
	private String zoneIn;

	@Column(name = "zone_b")
	private String zoneB;

	@Column(name = "zone_a")
	private String zoneA;
}
