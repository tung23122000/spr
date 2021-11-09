package dts.com.vn.ilink.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "bst_lookup_table_row", schema = "ilink")
@IdClass(BstLookupTableRowId.class)
public class BstLookupTableRow {

	@Id
	@Column(name = "table_id")
	private Long tableId;

	@Id
	@Column(name = "row_id")
	private Long rowId;

	@Column(name = "key")
	private String key;

	@Column(name = "value")
	private String value;

}
