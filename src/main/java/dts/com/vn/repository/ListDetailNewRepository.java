package dts.com.vn.repository;

import dts.com.vn.entities.ListDetailNew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListDetailNewRepository extends JpaRepository<ListDetailNew, Integer> {

	void deleteByIsdnListId(Long listId);

}
