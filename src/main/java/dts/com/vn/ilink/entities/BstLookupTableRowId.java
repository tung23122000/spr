package dts.com.vn.ilink.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BstLookupTableRowId implements Serializable {

	private static final long serialVersionUID = -4991804810981153886L;

	private Long tableId;

	private Long rowId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BstLookupTableRowId that = (BstLookupTableRowId) o;
		return Objects.equals(tableId, that.tableId) && Objects.equals(rowId, that.rowId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tableId, rowId);
	}
}
