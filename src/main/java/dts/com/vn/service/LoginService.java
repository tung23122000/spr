package dts.com.vn.service;

import dts.com.vn.entities.Account;
import dts.com.vn.entities.LoginHistory;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.AccountRepository;
import dts.com.vn.repository.LoginHistoryRepository;
import dts.com.vn.request.LoginRequest;
import dts.com.vn.response.AccountResponse;
import dts.com.vn.security.SecurityUtils;
import dts.com.vn.security.jwt.TokenProvider;
import dts.com.vn.util.WebUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginService {

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private LoginHistoryRepository loginHistoryRepository;

	private PasswordEncoder passwordEncoder;

	public LoginService(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = new BCryptPasswordEncoder();
	}

	public Map<String, Object> login(LoginRequest req, HttpServletRequest httpRequest) {
		String username = req.getUsername();
		Account account = accountRepository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new RestApiException(ErrorCode.USERNAME_INVALID));
		if (!passwordEncoder.matches(req.getPassword(), account.getPassword())) {
			throw new RestApiException(ErrorCode.PASSWORD_INVALID);
		}
		LoginHistory loginHistory = new LoginHistory();
		loginHistory.setAccountId(account.getAccountId());
		loginHistory.setLoginTime(Instant.now());
		loginHistory.setComputerName(WebUtil.getClientBrowser(httpRequest));
		loginHistory.setIpAddress(WebUtil.getClientIpAddr(httpRequest));
		loginHistory.setOnline(1);
		String tokenKey = tokenProvider.createToken(account, req.getRememberMe());
		loginHistory.setTokenKey(tokenKey);
		loginHistoryRepository.save(loginHistory);
		AccountResponse accountResponse = new AccountResponse(account);
		Map<String, Object> data = new HashMap<>();
		data.put("profile", accountResponse);
		data.put("token", tokenKey);
		return data;
	}

	public void logout() {
		String tokenKey = SecurityUtils.getCurrentUserTokenKey().get();
		LoginHistory loginHistory = loginHistoryRepository.findByTokenKey(tokenKey).orElse(null);
		if (Objects.isNull(loginHistory)) {
			return;
		}
		loginHistory.setTokenKey(null);
		loginHistory.setOnline(0);
		loginHistoryRepository.save(loginHistory);
	}
}
