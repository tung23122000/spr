package dts.com.vn.entities;

import java.time.Instant;
import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "account", schema = "public")
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "account_id")
  private Long accountId;

  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "email")
  private String email;

  @Column(name = "date_of_birth")
  private Instant dateOfBirth;

  @OneToOne
  @JoinColumn(name = "role")
  private Role role;

}
