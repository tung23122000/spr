package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "list_detail", schema = "public")
public class ListDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "list_detail_id")
	private Long listDetailId;

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

	public ListDetail() {
	}

	public ListDetail(Long listDetailId, Long isdnListId, String isdn, String status, Long countUsed, Integer prefixInfoId) {
		this.listDetailId = listDetailId;
		this.isdnListId = isdnListId;
		this.isdn = isdn;
		this.status = status;
		this.countUsed = countUsed;
		this.prefixInfoId = prefixInfoId;
	}
}
