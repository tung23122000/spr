package dts.com.vn.enumeration;

public enum ErrorCode {

	PACKAGE_PENDING("error.service.package.pending", "Gói cước đang được chỉnh sửa"),
	USERNAME_INVALID("error.username.invalid", "Tài khoản không tồn tại"),
	PASSWORD_INVALID("error.password.invalid", "Mật khẩu không đúng"),
	LOGOUT_FAILED("error.logout.failed", "Có lỗi xảy ra khi đăng xuất"),

	DATA_FAILED("error.data.failed", "Có lỗi với dữ liệu"),
	CLONE_SERVICE_PACKAGE_FAILED("error.clone.service.package", "Sao chép gói cước không thành công"),
	CLONE_SERVICE_PROGRAM_FAILED("error.clone.service.program", "Sao chép chương trình không thành công"),
	SERVICE_PROGRAM_NOT_FOUND("error.service.program.not.found", "Mã chương trình không tồn tại"),
	SERVICE_PACKAGE_NOT_FOUND("error.service.package.not.found", "Mã gói cước không tồn tại"),
	CONDITION_NOT_LOADED("error.condition.loaded", "Không tải được điều kiện"),
	SAVE_CONDITION_FAILED("error.condition.save", "Lưu điều kiện lỗi"),
	RESET_EXT_RETRY_NUM_FAILED("error.ext.retry.num.reset", "Reset EXT_RETRY_NUM lỗi"),
	SAVE_CONSTANT_FAILED("error.constant.save", "Lưu hằng số lỗi"),
	FIND_CONSTANT_FAILED("error.constant.find", "Tìm kiếm hằng số lỗi"),
	FIND_SUB_SERVICE_PACKAGE_FAILED("error.sub.service.package.find", "Lỗi trong quá trình tìm gói cước chặn"),
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
	ADD_SERVICE_PROGRAM_FAILED("error.add.failure", "Thêm mới chương trình lỗi"),
	VALIDATE_FAIL("error.service.validate.fail", "Lỗi sai định dạng"),
	GET_ALL_PREFIX_FAILED("error.get.all.prefix", "Lỗi khi tải đầu số"),
	UPDATE_PREFIX_FAILED("error.update.prefix", "Lỗi khi cập nhật đầu số"),
	GET_PREFIX_DETAIL_BY_ID("error.get.prefix.detail", "Lỗi khi tải thông tin chi tiết"),
	ADD_PREFIX_DETAIL_FAILED("error.add.prefix.detail", "Lỗi khi thêm mới chi tiết"),
	GET_ALL_PCRF_GROUP_FAILED("error.get.all.pcrf.group", "Lỗi khi tải nhóm PCRF"),
	ADD_PCRF_GROUP_FAILED("error.add.pcrf.group", "Lỗi khi thêm mới nhóm PCRF"),
	FIND_PCRF_GROUP_FAILED("error.find.pcrf.group", "Lỗi khi tìm kiếm nhóm PCRF"),
	ADD_MINUS_MONEY_FAILED("error.add.minus.money", "Lỗi khi thêm mới trừ tiền bậc thang"),
	DELETE_MINUS_MONEY_FAILED("error.delete.minus.money", "Lỗi khi xóa trừ tiền bậc thang"),
	DELETE_MAP_COMMAND_ALIAS_FAILED("error.delete.map.command.alias", "Lỗi khi xóa Command Alias"),
	DELETE_BUCKETS_INFO_FAILED("error.delete.buckets.info", "Lỗi khi xóa cấu hình IN"),
	DELETE_MAP_SERVICE_PACKAGE_FAILED("error.delete.map.service.package", "Lỗi khi xóa cấu hình Billing"),
	DELETE_SERVICE_PACKAGE_FAILED("error.delete.service.package", "Lỗi khi xóa gói cước"),
	DELETE_NDS_TYPE_PARAM_PROGRAM_FAILED("error.delete.nds.type.param.program", "Lỗi khi xóa cấu hình PCRF"),
	DELETE_SERVICE_INFO_FAILED("error.delete.service.info", "Lỗi khi xóa thông tin bổ sung"),
	DUPLICATE_SMS_MO("error.duplicate.sms.mo", "Cú pháp không được trùng trong cùng 1 thời điểm"),
	DUPLICATE_SOAP_REQUEST("error.duplicate.soap.request", "Soap Request không được trùng trong cùng 1 thời điểm"),
	DUPLICATE_PROGRAM_CODE("error.duplicate.program.code", "Mã chương trình không được trùng trong cùng 1 thời điểm"),
	DUPLICATE_SMS_RESPOND("error.duplicate.sms.respond", "SMS Respond đã tồn tại trong hệ thống"),
	ADD_ISDN_DETAIL_CENTER("error.add.isdn.detail.center", "Lỗi khi thêm mới CTKV"),
	UPDATE_ISDN_DETAIL_CENTER("error.update.isdn.detail.center", "Lỗi khi chỉnh sửa CTKV"),
	DELETE_ISDN_DETAIL_CENTER("error.delete.isdn.detail.center", "Lỗi khi chỉnh sửa CTKV"),

	UPDATE_BUCKETS_INFO_FAILED("error.update.buckets.info", "Lỗi khi chỉnh sửa cấu hình IN"),
	GET_ALL_MINUS_MONEY_FAILED("error.get.all.minus.money", "Lỗi khi tải trừ tiền bậc thang"),
	FIND_LOG_ACTION_FAILED("error.find.log.action", "Lỗi khi tải Log Action"),
	FIND_ISDN_LIST_FAILED("error.find.isdn.list", "Lỗi khi tải danh sách đối tượng"),
	SAVE_ISDN_LIST_FAILED("error.save.isdn.list", "Lỗi khi lưu danh sách đối tượng"),
	ADD_BUCKETS_INFO_FAILED("error.add.buckets.info", "Lỗi khi thêm cấu hình IN"),
	ADD_SERVICE_PACKAGE_LIST_FAILED("error.add.service.package.list", "Lỗi khi thêm danh sách đối tượng"),
	API_FAILED_UNKNOWN("error.api.failed.unknown", ""),
	REG_ID_NULL("error.reg.id.null", "Trường REG_ID trong truy vấn có giá trị NULL"),
	EXT_RETRY_NUM_NULL("error.ext.retry.num.null", "Trường EXT_RETRY_NUM trong truy vấn có giá trị NULL"),
	COMMAND_CODE_NULL("error.command.code.null", "Trường COMMAND_CODE trong truy vấn có giá trị NULL"),
	GROUP_CODE_NULL("error.group.code.null", "Trường GROUP_CODE trong truy vấn có giá trị NULL"),
	ISDN_NULL("error.isdn.null", "Trường ISDN trong truy vấn có giá trị NULL"),
	SERVICE_NUMBER_NULL("error.service.number.null", "Trường SERVICE_NUMBER trong truy vấn có giá trị NULL"),
	SOURCE_CODE_NULL("error.source.code.null", "Trường SOURCE_CODE trong truy vấn có giá trị NULL"),
	MISSING_DATA_FIELD("error.missing.data.field", "Thiếu trường dữ liệu"),
	SAVE_SMS_RESPOND_FAIL("error.save.sms.respond.fail", "Lỗi khi lưu sms respond"),
	FIND_ALL_SMS_RESPOND_FAIL("error.find.all.sms.respond.fail", "Lỗi khi tải sms respond"),
	DELETE_SMS_RESPOND_FAIL("error.delete.sms.respond.fail", "Lỗi khi xóa sms respond"),
	GET_WHITE_LIST_FAIL("error.get.white.list", "Lỗi khi tải danh sách White List"),
	GET_BLACK_LIST_FAIL("error.get.black.list", "Lỗi khi tải danh sách Black List"),
	SAVE_CCSP_INFO_FAIL("error.save.ccsp.info", "Lỗi khi lưu thông tin CCSP"),
	DUPLICATE_CCSP_INFO("error.duplicate.ccsp.info", "Đã tồn tại thông tin CCSP với giá trị FlowOne trong chương trình này"),
	EXIST_CCSP_VALUE("error.exist.ccsp.value", "CCSP Value đã tồn tại"),
	LIST_CONDITION_ID_NOT_FOUND("error.list.condition.not.found", "Không tìm thấy điều kiện"),
	SAVE_CONDITION_PROGRAM_FAIL("error.save.condition.program", "Lỗi khi thêm điều kiện"),
	GET_CONDITION_PROGRAM_FAIL("error.get.condition.program", "Lỗi khi tải điều kiện"),
	EXIST_MAP_CONDITION_PROGRAM("error.exist.map.condition.program", "Đã tồn tại bản ghi cấu hình của chương trình này"),
	UPDATE_CHECK_MAX_REGISTED("error.check.max.registed", "Lỗi khi lưu điều kiện CHECK_MAX_REGISTED"),

	TOKEN_NOT_EXIST("error.token.not.exist", ""),
	USER_NOT_MATCH("error.username.invalid", ""),
	NOT_FOUND("error.not.found", ""),

	// Giang add
	SERVICE_PACKAGE_ID_REQUIRED("error.null.servicePackageId", "ID gói cước cần clone không được bỏ trống."),
	CLONE_REQUEST_DATA_FAIL("error.data", "Dữ liệu service hoặc serviceType truyền lên không đúng."),

	//BinhDT add
	CONFIG_FLOW_CONDITION_ERROR("error.data","Insert data không hợp lệ."),
	CONFIG_FLOW_CONDITION_DELETE_ERROR("error.data","Xóa data không hợp lệ."),
	CONFIG_FLOW_CONDITION_UPDATE_ERROR("error.data","Update data không hợp lệ."),
	;
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
