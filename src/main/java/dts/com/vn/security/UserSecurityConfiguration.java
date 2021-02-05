package dts.com.vn.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import dts.com.vn.security.jwt.JWTConfigurer;
import dts.com.vn.security.jwt.TokenProvider;

@ComponentScan
@Configuration
@Import(SecurityProblemSupport.class)
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class UserSecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final TokenProvider tokenProvider;

  private final CorsFilter corsFilter;

  private final SecurityProblemSupport problemSupport;

  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

  public UserSecurityConfiguration(TokenProvider tokenProvider, CorsFilter corsFilter,
      SecurityProblemSupport problemSupport) {
    this.tokenProvider = tokenProvider;
    this.corsFilter = corsFilter;
    this.problemSupport = problemSupport;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/api/login").antMatchers("/swagger-ui.html");
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class).exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(problemSupport)
        .and().csrf().disable().headers().frameOptions().disable().and().sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().antMatcher("/api/**")
        .authorizeRequests().anyRequest().authenticated().and().apply(securityConfigurerAdapter());
  }

  private JWTConfigurer securityConfigurerAdapter() {
    return new JWTConfigurer(tokenProvider);
  }
}
