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
@EnableJpaRepositories(basePackages = {"dts.com.vn.ilarc.repository"},
		               entityManagerFactoryRef = "entityManagerFactoryIlarc",
		               transactionManagerRef = "transactionManagerIlarc")
public class PostgreDBilarcConfig {

	@Bean(name = "ilarcDataSource")
	@ConfigurationProperties(prefix = "spring.datasource.fourth")
	public DataSource ilarcDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean(name = "entityManagerFactoryIlarc")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory2(EntityManagerFactoryBuilder builder, @Qualifier("ilarcDataSource") DataSource dataSource) {
		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		return builder.dataSource(dataSource).properties(properties).packages("dts.com.vn.ilarc.entities").persistenceUnit("postgreSQL-ilarc").build();
	}

	@Bean(name = "transactionManagerIlarc")
	public PlatformTransactionManager transactionManager2(@Qualifier("entityManagerFactoryIlarc") EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}

}
