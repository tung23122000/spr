package dts.com.vn.config;

import dts.com.vn.ilink.constants.IlinkTableName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;


@Configuration
@EnableSwagger2
public class SpringFoxConfig {

	public static final String LKT_CHECK_CONDITIONS_TAG = "LKT_CHECK_CONDITIONS";

	public static final String LKT_COMMERCIAL_RFS_MAPPING_TAG = "LKT_COMMERCIAL_RFS_MAPPING";

	public static final String LKT_PACKAGE_INFO_TAG = "LKT_PACKAGE_INFO";

	public static final String LKT_FETCH_PARAMETERS_TAG = "LKT_FETCH_PARAMETERS";

	public static final String LKT_SMS_FORMAT_TAG = "LKT_SMS_FORMAT";

	@Bean
	public Docket createRestApi() {

		return new Docket(DocumentationType.SWAGGER_2)
				.pathMapping("/")
				.apiInfo(this.apiEndPointsInfo())
				.forCodeGeneration(true)
				.securityContexts(Collections.singletonList(this.securityContext()))
				.securitySchemes(Collections.singletonList(this.apiKey()))
				.useDefaultResponseMessages(false)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.regex("/api/.*")).build()
				.tags(new Tag(LKT_CHECK_CONDITIONS_TAG, "API cho bảng " + IlinkTableName.LKT_CHECK_CONDITIONS),
						new Tag(LKT_COMMERCIAL_RFS_MAPPING_TAG, "API cho bảng " + IlinkTableName.LKT_COMMERCIAL_RFS_MAPPING),
						new Tag(LKT_PACKAGE_INFO_TAG, "API cho bảng " + IlinkTableName.LKT_PACKAGE_INFO),
						new Tag(LKT_FETCH_PARAMETERS_TAG, "API cho bảng " + IlinkTableName.LKT_FETCH_PARAMETERS),
						new Tag(LKT_SMS_FORMAT_TAG, "API cho bảng " + IlinkTableName.LKT_SMS_FORMAT)
				);
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", HttpHeaders.AUTHORIZATION, "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(this.defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
	}

	private ApiInfo apiEndPointsInfo() {
		return new ApiInfoBuilder().title("Spring Boot REST API")
				.description("Beeland Management REST API").license("Apache 2.0")
				.licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html").version("1.0.0").build();
	}

}
