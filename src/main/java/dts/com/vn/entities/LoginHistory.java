package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "login_history", schema = "public")
public class LoginHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "account_id")
	private Long accountId;

	@Column(name = "login_time")
	private Instant loginTime;

	@Column(name = "computer_name")
	private String computerName;

	@Column(name = "ip_address")
	private String ipAddress;

	@Column(name = "token_key")
	private String tokenKey;

	@Column(name = "online")
	private Integer online;
}
