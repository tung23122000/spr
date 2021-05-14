package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "province", schema = "public")
public class Province {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "province_id")
	private Long provinceId;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "status")
	private String status;

	@Column(name = "in_province")
	private String inProvince;

	@Column(name = "profile")
	private String profile;

	@Column(name = "center")
	private String center;
}
