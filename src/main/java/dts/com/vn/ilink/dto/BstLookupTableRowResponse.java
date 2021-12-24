package dts.com.vn.ilink.dto;

import dts.com.vn.ilink.entities.BstLookupTableRow;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BstLookupTableRowResponse {

	private Long tableId;

	private Long rowId;

	private String key;

	private List<Value> values;

	public BstLookupTableRowResponse(BstLookupTableRow row) {
		this.setTableId(row.getTableId());
		this.setRowId(row.getRowId());
		this.setKey(row.getKey().replaceAll("\"", ""));
		String[] arrValue = row.getValue().replaceAll("\"", "").split(",,");
		List<Value> listValue = new ArrayList<>();
		for (String s : arrValue) {
			Value value = new Value(s);
			listValue.add(value);
		}
		this.setValues(listValue);
	}

}
