package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "isdn_detail_center", schema = "public")
public class IsdnDetailCenter {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "isdn_detail_center_id")
	private Long isdnDetailCenterId;

	@Column(name = "center_id")
	private String centerId;

	@Column(name = "isdn_prefix")
	private String isdnPrefix;

	@Column(name = "network")
	private String network;
}
