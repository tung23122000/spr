package dts.com.vn.ilink.dto;

import lombok.Data;

@Data
public class BstLookupTableRowRequest {

	private Long tableId;

	private Long rowId;

	private String key;

	private String value;

}
