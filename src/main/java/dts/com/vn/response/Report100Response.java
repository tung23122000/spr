package dts.com.vn.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report100Response  {

	private String phoneNumber;

	private Long quantity;

	private String command;

	private String sourceContent;

}
