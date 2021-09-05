package dts.com.vn.entities;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import java.io.Serializable;

@Data

public class JsonIsdn implements Serializable {

    private String isdn;

}
