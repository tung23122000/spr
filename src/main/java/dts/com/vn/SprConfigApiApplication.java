package dts.com.vn;

import dts.com.vn.config.FileStorageConfig;
import org.hibernate.dialect.PostgreSQL94Dialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.sql.Types;


@SpringBootApplication
@EnableConfigurationProperties({FileStorageConfig.class})
public class SprConfigApiApplication extends PostgreSQL94Dialect {

	public static void main(String[] args) {
		SpringApplication.run(SprConfigApiApplication.class, args);
	}

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(false);
		configuration.addAllowedOrigin("*");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.addAllowedOriginPattern("*");
		source.registerCorsConfiguration("/**", configuration);
		return new CorsFilter(source);
	}

	public SprConfigApiApplication() {
		this.registerColumnType(Types.JAVA_OBJECT, "jsonb");
	}

}
