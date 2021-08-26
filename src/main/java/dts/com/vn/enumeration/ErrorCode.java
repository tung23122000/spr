package dts.com.vn.enumeration;

public enum ErrorCode {

	PACKAGE_PENDING("error.service.package.pending", "Gói cước đang được chỉnh sửa"),
	USERNAME_INVALID("error.username.invalid", "Tài khoản không tồn tại"),
	PASSWORD_INVALID("error.password.invalid", "Mật khẩu không đúng"),
	LOGOUT_FAILED("error.logout.failed", "Có lỗi xảy ra khi đăng xuất"),

	DATA_FAILED("error.data.failed", "Có lỗi với dữ liệu"),
	SERVICE_PROGRAM_NOT_FOUND("error.service.program.not.found", "Mã chương trình không tồn tại"),
	SERVICE_PACKAGE_NOT_FOUND("error.service.package.not.found", "Mã gói cước không tồn tại"),
	CONDITION_NOT_LOADED("error.condition.loaded", "Không tải được điều kiện"),
	SAVE_CONDITION_FAILED("error.condition.save", "Lưu điều kiện lỗi"),
	ADD_COMMAND_ALIAS_FAILED("error.command.alias", "Thêm command alias lỗi"),
	UPDATE_COMMAND_ALIAS_FAILED("error.command.alias", "Thêm command alias lỗi"),
	EXTERNAL_SYSTEM_NOT_FOUND("error.external.system.not.found", "Nguồn dữ liệu lỗi"),
	MAP_SERVICE_PACKAGE_NOT_FOUND("error.map.service.package.not.found", "Không tải được dữ liệu billing"),
	GET_PERMISSION_FAILED("error.permission.failed", "Không tải được quyền của người dùng"),
	GET_ALL_USER_FAILED("error.get.all.user", "Lỗi khi tải người dùng"),
	EXIST_USER("error.user.exist", "Người dùng đã tồn tại"),
	ADD_USER_FAILED("error.add.user", "Có lỗi khi thêm người dùng"),
	USER_NOT_FOUND("error.user.not.found", "Không tìm thấy người dùng"),
	UPDATE_USER_FAILED("error.update.user", "Cập nhật người dùng lỗi"),
	DELETE_USER_FAILED("error.delete.user", "Xóa người dùng lỗi"),
	NDS_TYPE_PARAM_PROGRAM_NOT_FOUND("error.nds.type.param.program", "Không tìm thấy thông tin cấu hình PCRF"),
	UPDATE_SERVICE_PROGRAM_FAILED("error.update.failure", "Cập nhật chương trình lỗi"),
	VALIDATE_FAIL("error.service.validate.fail", "Lỗi sai định dạng"),
	GET_ALL_PREFIX_FAILED("error.get.all.prefix", "Lỗi khi tải đầu số"),
	UPDATE_PREFIX_FAILED("error.update.prefix", "Lỗi khi cập nhật đầu số"),
	GET_PREFIX_DETAIL_BY_ID("error.get.prefix.detail", "Lỗi khi tải thông tin chi tiết"),
	ADD_PREFIX_DETAIL_FAILED("error.add.prefix.detail", "Lỗi khi thêm mới chi tiết"),
	GET_ALL_PCRF_GROUP_FAILED("error.get.all.pcrf.group", "Lỗi khi tải nhóm PCRF"),
	ADD_PCRF_GROUP_FAILED("error.add.pcrf.group", "Lỗi khi thêm mới nhóm PCRF"),
	ADD_MINUS_MONEY_FAILED("error.add.minus.money", "Lỗi khi thêm mới trừ tiền bậc thang"),
	GET_ALL_MINUS_MONEY_FAILED("error.get.all.minus.money", "Lỗi khi tải trừ tiền bậc thang"),


	API_FAILED_UNKNOWN("error.api.failed.unknown", ""),

	TOKEN_NOT_EXIST("error.token.not.exist", ""),
	USER_NOT_MATCH("error.username.invalid", ""),
	NOT_FOUND("error.not.found", ""),

	// Giang add
	SERVICE_PACKAGE_ID_REQUIRED("error.null.servicePackageId", "ID gói cước cần clone không được bỏ trống."),
	CLONE_REQUEST_DATA_FAIL("error.data", "Dữ liệu service hoặc serviceType truyền lên không đúng.");

	private String errorCode;

	private String message;

	ErrorCode(String errorCode, String message) {
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
