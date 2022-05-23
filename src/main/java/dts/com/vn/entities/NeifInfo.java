package dts.com.vn.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "neif_info", schema = "public")
@NoArgsConstructor
public class NeifInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "neif_id")
    private Long neifId;

    @Column(name = "bonus_amount")
    private String bonusAmount;

    @Column(name= "card_number")
    private String cardNumber;

    @Column(name="insert_date")
    private Timestamp insertDate;

    @Column(name= "isdn")
    private String isdn;

    @Column(name= "ip_remote")
    private String ipRemote;

    @Column(name= "main_amount")
    private String mainAmount;

    @Column(name="neif_message")
    private String neifMessage;

    @Column(name="profile")
    private String profile;

    @Column(name="reg_date")
    private Timestamp regDate;

    @Column(name="status")
    private String status;

}
