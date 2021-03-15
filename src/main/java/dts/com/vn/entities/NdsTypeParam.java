package dts.com.vn.entities;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import dts.com.vn.request.NdsTypeParamProgramRequest;
import dts.com.vn.util.DateTimeUtil;
import lombok.Data;

@Data
@Entity
@Table(name = "nds_type_param", schema = "public")
public class NdsTypeParam {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "nds_type_param_key")
  private Long ndsTypeParamKey;

  @ManyToOne
  @JoinColumn(name = "package_id")
  private ServicePackage servicePackage;

  @Column(name = "nds_type")
  private String ndsType;

  @Column(name = "nds_param")
  private String ndsParam;

  @Column(name = "nds_value")
  private String ndsValue;

  @Column(name = "sta_datetime")
  private Instant staDatetime;

  @Column(name = "end_datetime")
  private Instant endDatetime;

  public NdsTypeParam() {}

  public NdsTypeParam(NdsTypeParamProgramRequest request, ServicePackage servicePackage) {
    this.servicePackage = servicePackage;
    this.ndsType = request.getNdsType();
    this.ndsParam = request.getNdsParam();
    this.ndsValue = request.getNdsValue();
    this.staDatetime =
        DateTimeUtil.convertStringToInstant(request.getStaDatetime(), "dd/MM/yyyy HH:mm:ss");
    this.endDatetime =
        DateTimeUtil.convertStringToInstant(request.getEndDatetime(), "dd/MM/yyyy HH:mm:ss");
  }
}
