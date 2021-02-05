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
@Table(name = "zone_info_all", schema = "public")
public class ZoneInfoAll {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "zone_info_id")
  private Long zoneInfoId;
  
  @Column(name = "code")
  private String code;
  
  @Column(name = "zone_city_loc")
  private String zoneCityLoc;
  
  @Column(name = "zone_w")
  private String zoneW;
  
  @Column(name = "zone_x")
  private String zoneX;
  
  @Column(name = "zone_y")
  private String zoneY;
  
  @Column(name = "zone_z")
  private String zoneZ;
  
  @Column(name = "city_loc_name")
  private String cityLocName;
  
  @Column(name = "districtw_name")
  private String districtwName;
  
  @Column(name = "districtx_name")
  private String districtxName;
  
  @Column(name = "districty_name")
  private String districtyName;
  
  @Column(name = "districtz_name")
  private String districtzName;
  
  @Column(name = "status")
  private Long status;
  
  @Column(name = "city")
  private String city;
}
