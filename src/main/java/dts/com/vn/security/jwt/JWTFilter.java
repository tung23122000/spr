package dts.com.vn.security.jwt;

import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filters incoming requests and installs a Spring Security principal if a header corresponding to a
 * valid user is found.
 */
public class JWTFilter extends GenericFilterBean {

	private TokenProvider tokenProvider;

	public JWTFilter(TokenProvider tokenProvider) {
		this.tokenProvider = tokenProvider;
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
	                     FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		tokenProvider.setAuthenticationByToken(resolveToken(httpRequest));
		filterChain.doFilter(servletRequest, servletResponse);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader(JWTConfigurer.AUTHORIZATION_HEADER);
		if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
			return null;
		}
		return bearerToken.substring(7, bearerToken.length());
	}
}
