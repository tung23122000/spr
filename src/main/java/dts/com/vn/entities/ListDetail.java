package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "list_detail", schema = "public")
public class ListDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "isdn_list_id")
	private Long isdnListId;

	@Column(name = "isdn")
	private String isdn;

	@Column(name = "status")
	private String status;

	@Column(name = "count_used")
	private Long countUsed;

	@Column(name = "prefix_info_id")
	private Integer prefixInfoId;
}
