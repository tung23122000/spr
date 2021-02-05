package dts.com.vn.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddServicePackageRequest {

  private Long servicePackageId;
  
  private Long serviceTypeId; //Id Loại gói cước

  private String mobType; // Loại thuê bao

  private String packageCode; // Mã gói cước

  private Long listAccount; // Danh sách thuê bao

  private String packageName; //Tên gói cước

  private String groupCode; // nhóm gói cước

  private String categoryData; // Loại Data (3G/4G)

  private String dateStart; // Ngày bắt đầu

  private String dateEnd;  //Ngày kết thúc
}
