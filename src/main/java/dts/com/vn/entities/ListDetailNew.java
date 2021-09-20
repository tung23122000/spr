package dts.com.vn.entities;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "list_detail_new")
@TypeDef(name = "JsonType", typeClass = JsonType.class)
public class ListDetailNew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_detail_id", nullable = false)
    private Long listDetailId;

    @Column(name = "isdn_list_id")
    private Long isdnListId;

    @Column(name = "data")
    @Type(type = "JsonType")
    private List<JsonIsdn> data;

    public ListDetailNew(Long listDetailId, Long isdnListId, List<JsonIsdn> data) {
        this.listDetailId = listDetailId;
        this.isdnListId = isdnListId;
        this.data = data;
    }
}