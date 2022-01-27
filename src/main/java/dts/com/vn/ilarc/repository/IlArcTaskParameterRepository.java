package dts.com.vn.ilarc.repository;

import dts.com.vn.ilarc.entities.IlArcTaskParameter;
import dts.com.vn.response.SourcesResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Repository
public interface IlArcTaskParameterRepository extends JpaRepository<IlArcTaskParameter, Long> {

	@Query(
			nativeQuery = true,
			value = "SELECT tp.request_id AS requestId,\n" +
					"tp.parameter_value AS parametersValue\n" +
					"FROM ilarc.il_arc_task_parameter tp\n" +
					"WHERE tp.request_id = :requestId \n" +
					"AND tp.parameter_name =  :parameterName"
	)
	Optional<SourcesResponse> findFlowSourceIlarc(Long requestId, String parameterName);


	@Query(
			nativeQuery = true,
			value = "SELECT DISTINCT rm.primary_subs_id as isdn\n" +
					"FROM ilarc.il_arc_request_message rm \n" +
					"WHERE rm.req_start_time BETWEEN :startDate AND :endDate\n" +
					"AND rm.req_status = '9'"
	)
	List<String> findIsdnHasFailReq(
			Timestamp startDate,
			Timestamp endDate
	);

	@Query(
			nativeQuery = true,
			value = "SELECT DISTINCT request_id \n" +
					"FROM ilarc.il_arc_request_message\n" +
					"WHERE primary_subs_id = :isdn\n" +
					"AND req_status ='9'\n" +
					"AND req_start_time BETWEEN :startDate AND :endDate"
	)
	List<Long> findReqIdByIsdn(
			String isdn,
			Timestamp startDate,
			Timestamp endDate
	);

	@Query(
			nativeQuery = true,
			value = "SELECT parameter_value\n" +
					"FROM ilarc.il_arc_task_parameter\n" +
					"WHERE request_id = :reqId \n" +
					"AND parameter_name = 'SMS_CONTENT'\n"
	)
	String findCommandByReqId(
			Long reqId
	);
}
