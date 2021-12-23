package dts.com.vn.ilink.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@NoArgsConstructor
public class CommercialMapping {

	private Long tableId;

	private Long rowId;

	private String commercialPackageCode;

	private String rfsMapping;

	public CommercialMapping(BstLookupTableRow row) {
		this.tableId = row.getTableId();
		this.rowId = row.getRowId();
		this.commercialPackageCode = StringUtils.isNotBlank(row.getKey())
				? row.getKey().replaceAll("\"", "") : row.getKey();
		this.rfsMapping = StringUtils.isNotBlank(row.getValue())
				? row.getValue().replaceAll("\"", "") : row.getValue();
	}

}
