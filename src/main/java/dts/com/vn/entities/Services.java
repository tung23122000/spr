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
