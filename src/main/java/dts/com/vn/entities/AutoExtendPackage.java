package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "auto_extend_package")
public class AutoExtendPackage {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "isdn")
	private String isdn;

	@Column(name = "package_id")
	private Long packageId;

	@Column(name = "program_id")
	private Long programId;

	@Column(name = "insert_date")
	private Instant insertDate;

	@Column(name = "last_retry_date")
	private Instant lastRetryDate;

	@Column(name = "soap_respond")
	private String soapRespond;

	@Column(name = "status")
	private String status;

	@Column(name = "retry_num")
	private Long retryNum;

}
