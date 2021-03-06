package dts.com.vn.ilink.repository;

import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.entities.BstLookupTableRowId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BstLookupTableRowRepository extends JpaRepository<BstLookupTableRow, BstLookupTableRowId> {

	@Query(nativeQuery = true, value = "SELECT r.* " +
			"FROM bst_lookup_table_row AS r " +
			"INNER JOIN bst_lookup_table AS tb ON tb.table_id = r.table_id " +
			"WHERE r.key = ?2 AND tb.name = ?1")
	BstLookupTableRow findByTableNameAndKey(String tableName, String key);

	@Query(nativeQuery = true, value = "SELECT r.* " +
			"FROM bst_lookup_table_row AS r " +
			"INNER JOIN bst_lookup_table AS tb ON tb.table_id = r.table_id " +
			"WHERE tb.name = ?1")
	List<BstLookupTableRow> findByTableName(String tableName);

	@Query(nativeQuery = true, value = "SELECT max(row_id) " +
			"FROM bst_lookup_table_row AS r " +
			"INNER JOIN bst_lookup_table AS lt ON r.table_id = lt.table_id " +
			"WHERE lt.name = ?1")
	Long getMaxRowId(String tableName);

	@Query(nativeQuery = true, value = "SELECT r.* " +
			"FROM bst_lookup_table_row AS r " +
			"INNER JOIN bst_lookup_table AS tb ON tb.table_id = r.table_id " +
			"WHERE tb.name = ?1 AND r.key LIKE ?2")
	Page<BstLookupTableRow> findAllWithSearch(String tableName, String search, Pageable pageable);

	@Query(nativeQuery = true, value = "SELECT r.* " +
			"FROM bst_lookup_table_row AS r " +
			"INNER JOIN bst_lookup_table AS tb ON tb.table_id = r.table_id " +
			"WHERE tb.name = ?1 ORDER BY r.row_id DESC")
	Page<BstLookupTableRow> findAllWithoutSearch(String tableName, Pageable pageable);

	BstLookupTableRow findByTableIdAndKey(Long tableId, String key);

}
