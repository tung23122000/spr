package dts.com.vn.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

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
