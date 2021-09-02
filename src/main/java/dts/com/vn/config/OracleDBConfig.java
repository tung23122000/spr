package dts.com.vn.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
		basePackages = {"dts.com.vn.oracle.repository"},
		entityManagerFactoryRef = "oracleEntityManagerFactory",
		transactionManagerRef = "oracleTransactionManager"
)
public class OracleDBConfig {

	@Bean(name = "oracleDatasource")
	@ConfigurationProperties(prefix = "spring.datasource.secondary")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "oracleEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory
			(EntityManagerFactoryBuilder builder,
			 @Qualifier("oracleDatasource") DataSource dataSource) {
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle9iDialect");
		return builder
				.dataSource(dataSource)
				.properties(properties)
				.packages("dts.com.vn.oracle.entities")
				.persistenceUnit("dbOracle")
				.build();
	}

	@Bean(name = "oracleTransactionManager")
	public PlatformTransactionManager transactionManager(
			@Qualifier("oracleEntityManagerFactory") EntityManagerFactory barEntityManagerFactory) {
		return new JpaTransactionManager(barEntityManagerFactory);
	}

}
