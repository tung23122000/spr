package dts.com.vn.response;

import java.util.Objects;
import dts.com.vn.entities.Account;
import dts.com.vn.util.DateTimeUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountResponse {

  private Long accountId;

  private String username;

  private String fullName;

  private String dateOfBirth;

  public AccountResponse() {}

  public AccountResponse(Account account) {
    super();
    this.accountId = account.getAccountId();
    this.username = account.getUsername();
    this.fullName = account.getFullName();
    this.dateOfBirth = Objects.nonNull(account.getDateOfBirth())
        ? DateTimeUtil.formatInstant(account.getDateOfBirth(), "dd/MM/yyyy HH:mm:ss")
        : "";
  }
}
