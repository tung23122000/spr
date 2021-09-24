package dts.com.vn.entities;

import dts.com.vn.request.IsdnDetailCenterRequest;
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

	public IsdnDetailCenter() {
	}

	public IsdnDetailCenter(IsdnDetailCenterRequest isdnDetailCenterRequest) {
		this.isdnDetailCenterId = isdnDetailCenterRequest.getIsdnDetailCenterId();
		this.centerId = isdnDetailCenterRequest.getCenterId();
		this.isdnPrefix = isdnDetailCenterRequest.getIsdnPrefix();
		this.network = isdnDetailCenterRequest.getNetwork();
	}
}
