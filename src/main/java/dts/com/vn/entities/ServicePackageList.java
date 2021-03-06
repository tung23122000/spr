package dts.com.vn.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "service_package_list", schema = "public")
@NoArgsConstructor
public class ServicePackageList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_package_list_id")
	private Long servicePackageListId;

	@Column(name = "package_id")
	private Long packageId;

	@Column(name = "isdn_list_id")
	private Long isdnListId;

	@Column(name = "sta_date")
	private Instant staDate;

	@Column(name = "end_date")
	private Instant endDate;

	@Column(name = "program_id")
	private Long programId;

	@Column(name = "msg_respond")
	private String msgRespond;

	public ServicePackageList(Long packageId, Long isdnListId, Instant staDate, Instant endDate, Long programId, String msgRespond) {
		this.packageId = packageId;
		this.isdnListId = isdnListId;
		this.staDate = staDate;
		this.endDate = endDate;
		this.programId = programId;
		this.msgRespond = msgRespond;
	}
}
