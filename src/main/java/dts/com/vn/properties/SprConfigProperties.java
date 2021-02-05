package dts.com.vn.properties;

import org.springframework.boot.SpringBootConfiguration;
import lombok.Data;

@Data
@SpringBootConfiguration
public class SprConfigProperties {
  private String encodePasswordApi;

  private String decodePasswordApi;

}
