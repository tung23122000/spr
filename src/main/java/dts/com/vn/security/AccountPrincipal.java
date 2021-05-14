package dts.com.vn.security;

import dts.com.vn.entities.Account;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AccountPrincipal implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Account account;

	private String tokenKey;

	private List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

	public AccountPrincipal(Account account) {
		this.account = account;
	}

	public AccountPrincipal(Account account, String tokenKey) {
		this.account = account;
		this.tokenKey = tokenKey;
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return grantedAuthorities;
	}

	@Override
	public String getPassword() {
		return account.getPassword();
	}

	@Override
	public String getUsername() {
		return account.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Account getAccount() {
		return account;
	}

	public Long getAccountId() {
		return account.getAccountId();
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}

}
