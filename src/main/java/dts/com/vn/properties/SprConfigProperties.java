package dts.com.vn.properties;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;

@Data
@SpringBootConfiguration
public class SprConfigProperties {
	private String encodePasswordApi;

	private String decodePasswordApi;

}
