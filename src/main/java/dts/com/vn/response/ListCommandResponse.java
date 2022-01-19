package dts.com.vn.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ListCommandResponse {

	private Long requestId;

	private String requestCommand;

	private Integer requestStatus;

}
