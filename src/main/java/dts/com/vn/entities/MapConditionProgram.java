package dts.com.vn.entities;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Table(name = "map_condition_program", schema = "public")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Data
public class MapConditionProgram {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	Long id;

	@Column(name = "program_id")
	Long programId;

	@ManyToOne
	@JoinColumn(name = "condition_id")
	ListCondition conditionId;

	@Column(name = "condition_value")
	@Type(type = "jsonb")
	private String conditionValue;

	@Column(name = "is_spr")
	private Boolean isSpr;

	@Column(name = "transaction")
	private String transaction;

}
