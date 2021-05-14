package dts.com.vn.security;

import dts.com.vn.entities.Account;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Data
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private Long accountId;

	private String username;

	private String password;

	public UserDetailsImpl(Long accountId, String username, String password) {
		super();
		this.accountId = accountId;
		this.username = username;
		this.password = password;
	}

	public static UserDetailsImpl build(Account nv) {
		return new UserDetailsImpl(nv.getAccountId(), nv.getUsername(), nv.getPassword());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}


}
