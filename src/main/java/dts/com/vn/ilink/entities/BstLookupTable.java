package dts.com.vn.ilink.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "bst_lookup_table", schema = "ilink")
@NoArgsConstructor
@AllArgsConstructor
public class BstLookupTable {

	@Id
	@Column(name = "table_id")
	private Long tableId;

	@Column(name = "name")
	private String tableName;

	@Column(name = "description")
	private String description;

	@Column(name = "row_version")
	private Integer rowVersion;

	@Column(name = "changed_by")
	private String changedBy;

}
