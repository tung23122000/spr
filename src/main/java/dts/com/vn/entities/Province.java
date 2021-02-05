package dts.com.vn.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

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
