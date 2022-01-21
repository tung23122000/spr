package dts.com.vn.ilarc.repository;

import dts.com.vn.ilarc.entities.IlArcTaskParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IlArcTaskParameterRepository extends JpaRepository<IlArcTaskParameter, Long> {

}
