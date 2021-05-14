package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@Entity
@Table(name = "isdn_retry_extend", schema = "public")
public class IsdnRetryExtend {

	@Id
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

	@Column(name = "isdn")
	private String isdn;

	@Column(name = "reg_datetime")
	private Instant regDatetime;

	@Column(name = "end_datetime")
	private Instant endDatetime;

	@Column(name = "expire_datetime")
	private Instant expireDatetime;

	@Column(name = "program_id")
	private Long programId;

	@Column(name = "package_code")
	private String packageCode;
}
