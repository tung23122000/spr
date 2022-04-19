package dts.com.vn.ilink.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sas_re_request_message", schema = "ilink")
public class SasReRequestMessage {

    @Id
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "primary_subs_id")
    private String primarySubsId;

    @Column(name = "received_time")
    private Timestamp receivedTime;

    @Column(name = "finished_time")
    private Timestamp finishedTime;

    @Column(name = "req_status")
    private Integer status;

    @Column(name = "smessage")
    private String message;

}
