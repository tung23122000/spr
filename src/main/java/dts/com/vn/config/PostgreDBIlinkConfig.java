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
		basePackages = {"dts.com.vn.ilink.repository"},
		entityManagerFactoryRef = "entityManagerFactoryIlink",
		transactionManagerRef = "transactionManagerIlink"
)
public class PostgreDBIlinkConfig {

	@Bean(name = "ilinkDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.third")
	public DataSource ilinkDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "entityManagerFactoryIlink")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory2(EntityManagerFactoryBuilder builder, @Qualifier("ilinkDataSource") DataSource dataSource) {
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		return builder
				.dataSource(dataSource)
				.properties(properties)
				.packages("dts.com.vn.ilink.entities")
				.persistenceUnit("postgreSQL-ilink")
				.build();
	}

	@Bean(name = "transactionManagerIlink")
	public PlatformTransactionManager transactionManager2(@Qualifier("entityManagerFactoryIlink") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}
