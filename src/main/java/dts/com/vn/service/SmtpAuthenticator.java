package dts.com.vn.service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class SmtpAuthenticator extends Authenticator {
  public SmtpAuthenticator() {
    super();
  }

  @Override
  public PasswordAuthentication getPasswordAuthentication() {
    String username = "selfcareinhn@mobifone.vn";
    String password = "selfcare@1234";
    if ((username != null) && (username.length() > 0) && (password != null)
        && (password.length() > 0)) {
      return new PasswordAuthentication(username, password);
    }
    return null;
  }

}
