package dts.com.vn.ilink.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "sas_re_task_parameter", schema = "ilink")
public class SasReTaskParameter {

    @EmbeddedId
    private EmbeddableWithoutId embeddableWithoutId;

    @Column(name = "parameterName")
    private String parameterName;

    @Column(name = "parametersType")
    private Long parametersType;

    @Column(name = "parametersValue")
    private String parametersValue;

    @Embeddable
    @Data
    public static class EmbeddableWithoutId implements Serializable {

        @Column(name = "requestId")
        private Long requestId;

        @Column(name = "taskId")
        private Long taskId;

    }

}
