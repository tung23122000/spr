package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Table(name = "sub_service_package", schema = "public")
public class SubServicePackage {

	@Column(name = "package_id")
	private Long packageId;

	@Column(name = "sub_package_id")
	private Long subPackageId;

	@Column(name = "is_active")
	private String isActive;
}
