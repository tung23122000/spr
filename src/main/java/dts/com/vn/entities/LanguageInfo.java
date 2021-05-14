package dts.com.vn.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "language_info", schema = "public")
public class LanguageInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "language_id")
	private Long languageId;

	@Column(name = "language_code")
	private String languageCode;

	@Column(name = "language_name")
	private String languageName;

	@Column(name = "status")
	private String status;

	@Column(name = "language_in")
	private String languageIn;
}
