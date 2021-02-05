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
@Table(name = "blacklist_package_list", schema = "public")
public class BlacklistPackageList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "blacklist_package_list_id", nullable = false)
  private Long blacklistPackageListId;

  @Column(name = "package_id", nullable = false)
  private Long packageId;

  @Column(name = "program_id")
  private Long programId;


  @Column(name = "isdn_list_id", nullable = false)
  private Long isdnListId;

  @Column(name = "sta_date", nullable = false)
  private Instant staDate;

  @Column(name = "end_date")
  private Instant endDate;
}
