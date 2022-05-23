package dts.com.vn.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request_summary", schema = "public")
public class RequestSummary {

    @Id
    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "isdn")
    private String isdn;

    @Column(name = "received_date")
    private Timestamp receivedDate;

    @Column(name = "response_date")
    private Timestamp responseDate;

    @Column(name = "request_command")
    private String requestCommand;

    @Column(name = "response_message")
    private String responseMessage;

    @Column(name = "response_error")
    private String responseError;

    @Column(name = "status")
    private Integer status;

    @Column(name = "chanel")
    private String chanel;

    @Column(name = "transaction")
    private String transaction;

}
