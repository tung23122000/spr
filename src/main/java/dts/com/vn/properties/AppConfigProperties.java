package dts.com.vn.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;

@Data
@SpringBootConfiguration
public class AppConfigProperties {

  @Value("${app.config-path}")
  private String appConfigPath;

  @Value("${app.scan-folder}")
  private String scanFolder;
}
