package dts.com.vn.ilink.service.impl;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.repository.BstLookupTableRowRepository;
import dts.com.vn.ilink.service.ConditionService;
import dts.com.vn.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConditionServiceImpl implements ConditionService {

	private final BstLookupTableRowRepository bstLookupTableRowRepository;

	@Autowired
	public ConditionServiceImpl(BstLookupTableRowRepository bstLookupTableRowRepository) {
		this.bstLookupTableRowRepository = bstLookupTableRowRepository;
	}

	@Override
	public ApiResponse findConditionByProgramCodeAndTransaction(String programCode, String transaction) {
		ApiResponse response;
		String key = "\"" + programCode + "#" + transaction + "\"";
		BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey("LKT_CHECK_CONDITIONS", key);
		if (record != null) {
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), record.getValue(),
					null, "Lấy danh sách điều kiện từ ilink thành công");
		} else {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, null, "Không tồn tại bản ghi nào");
		}
		return response;
	}

}
