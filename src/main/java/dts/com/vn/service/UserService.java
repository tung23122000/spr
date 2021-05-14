package dts.com.vn.service;

import dts.com.vn.entities.Account;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.AccountRepository;
import dts.com.vn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
	private AccountRepository accountRepository;

	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;

	@Autowired
	public UserService(AccountRepository accountRepository, UserRepository userRepository) {
		this.accountRepository = accountRepository;
		this.userRepository = userRepository;
	}

	public List<Account> getAllUser() {
		List<Account> list = accountRepository.findAll();
		return list;
	}


	public Account add(Account request) {
		Account account = accountRepository.getAccountByUsername(request.getUsername());
		if (account == null) {
			this.passwordEncoder = new BCryptPasswordEncoder();
			request.setPassword(this.passwordEncoder.encode(request.getPassword()));
			return accountRepository.save(request);
		}
		return null;
	}

	public Account findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new RestApiException(ErrorCode.API_FAILED_UNKNOWN));
	}

	public Account update(Long id, Account account) {
		Account returnAccount = userRepository.findById(id)
				.orElseThrow(() -> new RestApiException(ErrorCode.API_FAILED_UNKNOWN));
		returnAccount.setRole(account.getRole());
		returnAccount.setFullName(account.getFullName());
		returnAccount.setEmail(account.getEmail());
		returnAccount.setDateOfBirth(account.getDateOfBirth());

		return accountRepository.save(returnAccount);
	}


	public Account delete(Long id) {
		Account returnAccount = userRepository.findById(id)
				.orElseThrow(() -> new RestApiException(ErrorCode.API_FAILED_UNKNOWN));
		accountRepository.delete(returnAccount);
		return returnAccount;
	}
}
