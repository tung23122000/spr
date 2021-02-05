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
@Table(name = "isdn_detail_center", schema = "public")
public class IsdnDetailCenter {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "isdn_detail_center_id")
  private Long isdnDetailCenterId;

  @Column(name = "center_id")
  private String centerId;

  @Column(name = "isdn_prefix")
  private String isdnPrefix;

  @Column(name = "network")
  private String network;
}
