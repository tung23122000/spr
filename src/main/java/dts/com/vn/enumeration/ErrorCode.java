package dts.com.vn.enumeration;

public enum ErrorCode {

	PACKAGE_PENDING("error.service.package.pending", "Gói cước đang được chỉnh sửa"),
	USERNAME_INVALID("error.username.invalid", "Tài khoản không tồn tại"),
	PASSWORD_INVALID("error.password.invalid", "Mật khẩu không đúng"),
	LOGOUT_FAILED("error.logout.failed", "Có lỗi xảy ra khi đăng xuất"),


	API_FAILED_UNKNOWN("error.api.failed.unknown", ""),
	TOKEN_NOT_EXIST("error.token.not.exist", ""),
	USER_NOT_MATCH("error.username.invalid", ""),
	UPDATE_FAILURE("error.update.failure", ""),
	SERVICE_PACKAGE_NOT_FOUND("error.service.package.not.found", ""),
	SERVICE_PROGRAM_NOT_FOUND("error.service.program.not.found", ""),
	EXTERNAL_SYSTEM_NOT_FOUND("error.external.system.not.found", ""),
	MAP_SERVICE_PACKAGE_NOT_FOUND("error.map.service.package.not.found", ""),
	NOT_FOUND("error.not.found", ""),
	VALIDATE_FAIL("error.service.validate.fail", "")
	;

	private String errorCode;

	private String message;

	private ErrorCode(String errorCode, String message) {
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
