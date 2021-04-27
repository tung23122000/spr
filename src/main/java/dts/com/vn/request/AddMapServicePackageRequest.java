package dts.com.vn.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddMapServicePackageRequest {

  private Long mapId; // billing id

  private Long extSystemId; // id hệ thống (Tên hệ thống)

  private Long programId; // id ma khuyen mai (Chương trình)
  
  private String programDescription; // (Chương trình)

  private Long packageId; // service package Id

  private String startDate; // Ngày bắt đầu

  private String endDate; // ngày kết thúc

  private String promCode; // mã khuyến mãi

  private String mobType; // loại thuê bao

  private String promDays; // số ngày khuyến mãi

  private String onOff; // Online/Offline

  private String regMapCode;// Mã đăng ký

  private String delMapCode; // Mã hủy

  private String chgMapCode; // Mã đổi
}
