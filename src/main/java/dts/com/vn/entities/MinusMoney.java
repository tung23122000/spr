package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "minus_money_ladder", schema = "public")
public class MinusMoney {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "minus_money_ladder_id")
    private Long minusMoneyLadderId;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private ServicePackage servicePackage;

    @ManyToOne
    @JoinColumn(name = "program_id")
    private ServiceProgram serviceProgram;

    @Column(name = "minus_amount")
    private Long minusAmount;

    @Column(name = "expired_day")
    private Long expiredDay;
}
