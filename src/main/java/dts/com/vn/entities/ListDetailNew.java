package dts.com.vn.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "list_detail_new")
//@TypeDef(name = "JsonType", typeClass = JsonType.class)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor
public class ListDetailNew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_detail_id", nullable = false)
    private Long listDetailId;

    @Column(name = "isdn_list_id")
    private Long isdnListId;

    @Column(name = "details")
    @Type(type = "jsonb")
    private Object details;

    @Column(name="status")
    private String status;

    public ListDetailNew(Long listDetailId, Long isdnListId, Object data,String status) {
        this.listDetailId = listDetailId;
        this.isdnListId = isdnListId;
        this.details = data;
        this.status = status;
    }

}