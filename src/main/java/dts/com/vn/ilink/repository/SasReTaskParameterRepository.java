package dts.com.vn.ilink.repository;

import dts.com.vn.ilink.entities.SasReTaskParameter;
import dts.com.vn.response.CommandResponse;
import dts.com.vn.response.SourcesResponse;
import dts.com.vn.response.ilink.ILRequestParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;


@Repository
public interface SasReTaskParameterRepository extends JpaRepository<SasReTaskParameter, Long> {

    @Query(nativeQuery = true,
            value = "SELECT count(rm.request_id) \n"
                    + "FROM ilink.sas_re_task_parameter tp\n"
                    + "    INNER JOIN ilink.sas_re_request_message rm ON tp.request_id = rm.request_id\n"
                    + "WHERE tp.parameter_value LIKE CONCAT('%',:parameterValue,'%')\n"
                    + "  AND tp.parameter_name = 'SMS_CONTENT'\n"
                    + "  AND rm.req_status = 7\n"
                    + "AND rm.received_time BETWEEN :regDate AND :endDate \n")
    Integer findAllSuccessByParameterValueInIlink(String parameterValue, Timestamp regDate, Timestamp endDate);

    @Query(nativeQuery = true,
            value = "SELECT count(rm.request_id) \n"
                    + "FROM ilink.sas_re_task_parameter tp\n"
                    + "    INNER JOIN ilink.sas_re_request_message rm ON tp.request_id = rm.request_id\n"
                    + "WHERE tp.parameter_value LIKE CONCAT('%',:parameterValue,'%')\n"
                    + "  AND tp.parameter_name = 'SMS_CONTENT'\n"
                    + "  AND rm.req_status = 9\n"
                    + "AND rm.received_time BETWEEN :regDate AND :endDate \n")
    Integer findAllFailByParameterValueInIlink(String parameterValue, Timestamp regDate, Timestamp endDate);

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
                    "  AND rm.received_time BETWEEN :regDate AND :endDate")
    List<CommandResponse> findByListCommand(String packageCode, String keytransaction, Timestamp regDate,
                                            Timestamp endDate);

    @Query(nativeQuery = true, value = "SELECT tp.request_id AS requestId,\n" +
            "       tp.parameter_value AS parametersValue\n" +
            "FROM ilink.sas_re_task_parameter tp\n" +
            "WHERE tp.request_id = :requestId \n" +
            "  AND tp.parameter_name = :parameterName")
    Optional<SourcesResponse> findFlowSourceIlink(Long requestId, String parameterName);

    @Query(
            nativeQuery = true,
            value = "SELECT DISTINCT rm.primary_subs_id as isdn\n" +
                    "FROM ilink.sas_re_request_message rm \n" +
                    "WHERE rm.received_time BETWEEN :startDate AND :endDate\n" +
                    "AND rm.req_status = '9'"
    )
    List<String> findIsdnHasFailReq(
            Timestamp startDate,
            Timestamp endDate
    );

    @Query(
            nativeQuery = true,
            value = "SELECT DISTINCT request_id \n" +
                    "FROM ilink.sas_re_request_message\n" +
                    "WHERE primary_subs_id = :isdn\n" +
                    "AND req_status ='9'\n" +
                    "AND received_time BETWEEN :startDate AND :endDate"
    )
    List<Long> findReqIdByIsdn(
            String isdn,
            Timestamp startDate,
            Timestamp endDate
    );

    @Query(
            nativeQuery = true,
            value = "SELECT parameter_value\n" +
                    "FROM ilink.sas_re_task_parameter\n" +
                    "WHERE request_id = :reqId \n" +
                    "AND parameter_name = 'SMS_CONTENT'\n"
    )
    String findCommandByReqId(
            Long reqId
    );


    @Query(nativeQuery = true,
            value = "SELECT parameter_name AS paramname," +
                    "parameter_type AS paramtype," +
                    "task_id AS taskid," +
                    "request_id AS requestid," +
                    "parameter_value AS paramvalue " +
                    "FROM ilink.sas_re_task_parameter " +
                    "WHERE request_id = ?1")
    List<ILRequestParameter> findAllByRequestId(Long requestId);

}
