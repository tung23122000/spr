package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

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
