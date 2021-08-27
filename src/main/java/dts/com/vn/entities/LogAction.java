package dts.com.vn.entities;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "log_action", schema = "public")
public class LogAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_action_id")
    private Long logActionId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "action")
    private String action;

    @Column(name = "table_action")
    private String tableAction;

    @Column(name = "id_action")
    private Long idAction;

    @Column(name = "time_action")
    private Date timeAction;

    @Column(name = "old_value")
    private String oldValue;

    @Column(name = "new_value")
    private String newValue;

}
