package dts.com.vn.response;

/**
 * Description - Nhận kết quả trả về từ DB
 *
 * @author - binhDT
 * @created - 17/01/2022
 */
public interface CommandResponse {

	Long getRequestId();

	String getRequestCommand();

	Integer getRequestStatus();


}
