package dts.com.vn.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "isdn_location_monthly", schema = "public")
public class IsdnLocationMonthly {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "isdn_location_monthly_id")
  private Long isdnLocationMonthlyId;

  @Column(name = "isdn")
  private String isdn;

  @Column(name = "city_loc")
  private String cityLoc;

  @Column(name = "center_code")
  private String centerCode;

  @Column(name = "month")
  private String month;

  @Column(name = "insert_date")
  private Instant insertDate;
}
