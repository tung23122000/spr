package dts.com.vn.ilink.dto;

import lombok.Data;

import java.util.List;

@Data
public class BstLookupTableRowRequestCustom {

	private Long tableId;

	private Long rowId;

	private String key;

	private List<Value> values;

}
