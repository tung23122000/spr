package dts.com.vn.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "callbarring_info", schema = "public")
public class CallbarringInfo {

  @Id
  @Column(name = "isdn", nullable = false)
  private Long isdn;

  @Column(name = "phone")
  private String phone;

  @Column(name = "type")
  private String type;

  @Column(name = "bare_phone")
  private String barePhone;

  @Column(name = "tree_ref")
  private String treeRef;

  @Column(name = "status")
  private String status;

  @Column(name = "create_date")
  private Instant createDate;
}
