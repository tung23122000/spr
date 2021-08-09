package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sub_service_package", schema = "public")
public class SubServicePackage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sub_service_package_id")
	private Long subServicePackageId;

	@Column(name = "package_id")
	private Long packageId;

	@Column(name = "sub_package_id")
	private Long subPackageId;
}
