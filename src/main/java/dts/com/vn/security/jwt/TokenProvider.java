package dts.com.vn.security.jwt;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import dts.com.vn.entities.Account;
import dts.com.vn.entities.LoginHistory;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.properties.JwtProperties;
import dts.com.vn.repository.AccountRepository;
import dts.com.vn.repository.LoginHistoryRepository;
import dts.com.vn.security.AccountPrincipal;
import dts.com.vn.security.SecurityUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;


@Component
public class TokenProvider {

  private static final String AUTHORITIES_KEY = "auth";

  @Autowired
  private JwtProperties jwtProperties;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private LoginHistoryRepository loginHistoryRepository;

  public String createToken(Account nhanVien, boolean rememberMe) {
    long plusTime = rememberMe ? jwtProperties.getTokenValidityInSecondsForRememberMe()
        : jwtProperties.getTokenValidityInSeconds();
    Date validity = Date.from(Instant.now().plusSeconds(plusTime));
    String subject = String.valueOf(nhanVien.getAccountId());
    return Jwts.builder().setSubject(subject)
        .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey()).setExpiration(validity)
        .compact();
  }

  public String createTokenNotExpire(Authentication authentication) {
    String authorities = authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
    AccountPrincipal nhanVienPrincipal = (AccountPrincipal) authentication.getPrincipal();
    String subject = String.valueOf(nhanVienPrincipal.getAccountId());
    return Jwts.builder().setSubject(subject).setIssuedAt(new Date())
        .claim(AUTHORITIES_KEY, authorities)
        .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecretKey()).compact();
  }

  public void setAuthenticationByToken(String token) {
    try {
      if (!StringUtils.hasText(token)) {
        return;
      }
      Claims claims = validateAndParseToken(token);
      if (Objects.isNull(claims)) {
        return;
      }
      LoginHistory lsDangNhapNV = loginHistoryRepository.findByTokenKey(token)
          .orElseThrow(() -> new RestApiException(ErrorCode.TOKEN_NOT_EXIST));
      Long accountId = Long.valueOf(claims.getSubject());
      if (!lsDangNhapNV.getAccountId().equals(accountId)) {
        throw new RestApiException(ErrorCode.USER_NOT_MATCH);
      }
      Account nhanVien = accountRepository.findById(accountId)
          .orElseThrow(() -> new RestApiException(ErrorCode.USER_NOT_MATCH));
      UserDetails userDetails = new AccountPrincipal(nhanVien, token);
      Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
          SecurityUtils.getCurrentUserJWT(), userDetails.getAuthorities());
      SecurityContextHolder.getContext().setAuthentication(authentication);
    } catch (Exception e) {
    }
  }

  public boolean validateToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      // logger.logTrace("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      // logger.logTrace("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      // logger.logTrace("Expired JWT token: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      // logger.logTrace("Unsupported JWT token: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      // logger.logTrace("JWT token compact of handler are invalid: {}", e.getMessage());
    }
    return false;
  }

  public Claims validateAndParseToken(String token) {
    Claims claims = null;
    try {
      claims =
          Jwts.parser().setSigningKey(jwtProperties.getSecretKey()).parseClaimsJws(token).getBody();
    } catch (SignatureException e) {
      // logger.logTrace("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      // logger.logTrace("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      // logger.logTrace("Expired JWT token: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      // logger.logTrace("Unsupported JWT token: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      // logger.logTrace("JWT token compact of handler are invalid: {}", e.getMessage());
    } catch (Exception e) {
    }
    return claims;
  }

}
