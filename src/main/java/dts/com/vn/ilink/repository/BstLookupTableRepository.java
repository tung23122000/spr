package dts.com.vn.ilink.repository;

import dts.com.vn.ilink.entities.BstLookupTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BstLookupTableRepository extends JpaRepository<BstLookupTable, Long> {

	@Query(value = "SELECT t.tableId FROM BstLookupTable t WHERE t.tableName = ?1")
	Long findByName(String tableName);

}
