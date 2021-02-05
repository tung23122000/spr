package dts.com.vn.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "service_package_list", schema = "public")
public class ServicePackageList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "service_package_list_id")
  private Long servicePackageListId;

  @Column(name = "package_id")
  private Long packageId;

  @Column(name = "isdn_list_id")
  private Long isdnListId;

  @Column(name = "sta_date")
  private Instant staDate;

  @Column(name = "end_date")
  private Instant endDate;

  @Column(name = "program_id")
  private Long programId;

  @Column(name = "msg_respond")
  private String msgRespond;
}
