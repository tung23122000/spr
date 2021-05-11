package dts.com.vn.response;

import java.util.Objects;
import dts.com.vn.entities.Account;
import dts.com.vn.entities.Role;
import dts.com.vn.util.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponse {

  private Long accountId;

  private String username;

  private String password;


  private String fullName;

  private String email;

  private String dateOfBirth;

  private Role role;

  public AccountResponse() {}

  public AccountResponse(Account account) {
    super();
    this.accountId = account.getAccountId();
    this.username = account.getUsername();
    this.password = account.getPassword();
    this.fullName = account.getFullName();
    this.email = account.getEmail();
    this.dateOfBirth = Objects.nonNull(account.getDateOfBirth())
        ? DateTimeUtil.formatInstant(account.getDateOfBirth(), "dd/MM/yyyy HH:mm:ss")
        : "";
    this.role = account.getRole();
  }
}
