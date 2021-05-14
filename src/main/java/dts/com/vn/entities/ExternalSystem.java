package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "external_system", schema = "public")
public class ExternalSystem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ext_system_id")
	private Long extSystemId;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "status")
	private String status;

	@Column(name = "system_type")
	private String systemType;

	@Column(name = "system_program")
	private String systemProgram;
}
