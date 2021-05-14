package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "blacklist_package_list", schema = "public")
public class BlacklistPackageList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "blacklist_package_list_id", nullable = false)
	private Long blacklistPackageListId;

	@Column(name = "package_id", nullable = false)
	private Long packageId;

	@Column(name = "program_id")
	private Long programId;


	@Column(name = "isdn_list_id", nullable = false)
	private Long isdnListId;

	@Column(name = "sta_date", nullable = false)
	private Instant staDate;

	@Column(name = "end_date")
	private Instant endDate;
}
