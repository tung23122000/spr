package dts.com.vn.ilink.repository;

import dts.com.vn.ilink.entities.SasReTaskParameter;
import dts.com.vn.response.CommandResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface SasReTaskParameterRepository extends JpaRepository<SasReTaskParameter, Long> {

	@Query(
			nativeQuery = true,
			value =
					"SELECT count(rm.request_id) \n"
							+ "FROM sas_re_task_parameter tp\n"
							+ "    INNER JOIN sas_re_request_message rm ON tp.request_id = rm.request_id\n"
							+ "WHERE tp.parameter_value LIKE CONCAT('%',:parameterValue,'%')\n"
							+ "  AND tp.parameter_name = 'SMS_CONTENT'\n"
							+ "  AND rm.req_status = 9\n"
							+ "AND rm.finished_time BETWEEN :regDate AND :endDate \n"
							+ "\n")
	Integer findAllFailByParameterValueAndDate(
			String parameterValue, Timestamp regDate, Timestamp endDate);

	@Query(nativeQuery = true,
			value = "SELECT rm.request_id AS requestId,\n" +
					"       tp.parameter_value AS requestCommand,\n" +
					"       rm.req_status AS requestStatus\n" +
					"FROM sas_re_task_parameter tp\n" +
					"         INNER JOIN sas_re_request_message rm\n" +
					"                    ON tp.request_id = rm.request_id\n" +
					"WHERE tp.parameter_value LIKE CONCAT('%', :packageCode, '%')\n" +
					"  AND tp.parameter_value LIKE CONCAT('%', :keytransaction, '%')\n" +
					"  AND tp.parameter_name = 'SMS_CONTENT'\n" +
					"  AND rm.finished_time BETWEEN :regDate AND :endDate")
	List<CommandResponse> findByListCommand(String packageCode, String keytransaction, Timestamp regDate, Timestamp endDate);

}
