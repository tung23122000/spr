package dts.com.vn.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "sub_service_package", schema = "public")
public class SubServicePackage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "sub_service_package_id")
  private Long subServicePackageId;

  @ManyToOne
  @JoinColumn(name = "packageId")
  private ServicePackage servicePackage;

  @ManyToOne
  @JoinColumn(name = "sub_package_id")
  private ServicePackage subServicePackage;
}
