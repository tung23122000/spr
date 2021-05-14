package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "service_type", schema = "public")
public class ServiceType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_type_id")
	private Long serviceTypeId;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "status")
	private String status;

	@Column(name = "service_id")
	private Long serviceId;

	@Column(name = "display_status")
	private String displayStatus;
}
