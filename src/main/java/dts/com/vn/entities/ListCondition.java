package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "list_condition", schema = "public")
@Data
public class ListCondition {

	@Id
	@Column(name = "id")
	Integer id;

	@Column(name = "condition_name")
	String conditionName;

	@Column(name = "ilink_service_name")
	String ilinkServiceName;

	@Column(name = "description")
	String description;

	@Column(name="is_package")
	Boolean isPackage;

	@Column(name = "status")
	Boolean status;
}
