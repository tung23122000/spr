package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "services", schema = "public")
public class Services {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "service_id")
	private Long serviceId;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "service_name")
	private String serviceName;

	@Column(name = "status")
	private String status;
}
