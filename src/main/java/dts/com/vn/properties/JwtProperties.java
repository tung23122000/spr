package dts.com.vn.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;

@Data
@SpringBootConfiguration
public class JwtProperties {

	@Value("${security.authentication.jwt.secret-key}")
	private String secretKey;

	@Value("${security.authentication.jwt.token-validity-in-seconds:86400}")
	private Long tokenValidityInSeconds;

	@Value("${security.authentication.jwt.token-validity-in-seconds-for-remember-me:864000}")
	private Long tokenValidityInSecondsForRememberMe;
}
