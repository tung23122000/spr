package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@Table(name = "isdn_list", schema = "public")
public class IsdnList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "isdn_list_id")
	private Long isdnListId;

	@Column(name = "name")
	private String name;

	@Column(name = "create_date")
	private Instant createDate;

	@Column(name = "cv_code_list")
	private String cvCodeList;

	@Column(name = "is_display")
	private String isDisplay;

	@Column(name = "end_date")
	private Instant endDate;

	@Column(name = "list_type")
	private String listType;

}
