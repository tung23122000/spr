package dts.com.vn.ilink.repository;

import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.entities.BstLookupTableRowId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BstLookupTableRowRepository extends JpaRepository<BstLookupTableRow, BstLookupTableRowId> {

	@Query(nativeQuery = true, value = "SELECT r.* " +
			"FROM bst_lookup_table_row AS r " +
			"INNER JOIN bst_lookup_table AS tb ON tb.table_id = r.table_id " +
			"WHERE r.key = ?2 AND tb.name = ?1")
	BstLookupTableRow findByTableNameAndKey(String tableName, String key);

}