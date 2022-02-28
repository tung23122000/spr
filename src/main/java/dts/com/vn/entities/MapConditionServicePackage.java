package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "map_condition_service_package", schema = "public")
public class MapConditionServicePackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "package_id")
    private Long packageId;

    @Column(name = "program_id")
    private Long programId;

    @ManyToOne
    @JoinColumn(name = "condition_id")
    private Condition condition;

    @Column(name = "is_confirm")
    private Boolean isConfirm;

    @Column(name = "message_mt")
    private String messageMt;

    @Column(name = "is_soap_confirm")
    private Boolean isSoapConfirm;

    @Column(name = "is_change")
    private Boolean isChange;

}
