package dts.com.vn.ilink.service.impl;

import dts.com.vn.entities.ListCondition;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.repository.BstLookupTableRowRepository;
import dts.com.vn.ilink.service.ConditionService;
import dts.com.vn.repository.ListConditionRepository;
import dts.com.vn.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class ConditionServiceImpl implements ConditionService {

	private final ListConditionRepository listConditionRepository;

	private final BstLookupTableRowRepository bstLookupTableRowRepository;

	@Autowired
	public ConditionServiceImpl(ListConditionRepository listConditionRepository,
	                            BstLookupTableRowRepository bstLookupTableRowRepository) {
		this.listConditionRepository = listConditionRepository;
		this.bstLookupTableRowRepository = bstLookupTableRowRepository;
	}

	/**
	 * Description - Lấy danh sách các điều kiện để config
	 *
	 * @return any
	 * @author - giangdh
	 * @created - 11/12/2021
	 */
	@Override
	public ApiResponse findAllCondition() {
		ApiResponse response;
		List<ListCondition> listConditions = listConditionRepository.findAll();
		response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(),
				listConditions, null, "Lấy danh sách điều kiện thành công");
		return response;
	}

	/**
	 * Description - Lấy danh sách điều kiện của 1 chương trình
	 *
	 * @param programCode - Mã chương trình
	 * @param transaction - Luồng
	 * @return ApiResponse
	 * @author - giangdh
	 * @created - 11/12/2021
	 */
	@Override
	public ApiResponse findConditionByProgramCodeAndTransaction(String programCode, String transaction) {
		ApiResponse response;
		String key = "\"" + programCode + "#" + transaction + "\"";
		BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey("LKT_CHECK_CONDITIONS", key);
		if (record != null) {
			List<HashMap<String, String>> listCondition = new ArrayList<>();
			if (record.getValue() != null) {
				List<String> listConditonFromIlink = new ArrayList<>(Arrays.asList(record.getValue().split(",,")));
				for (int i = 0; i < listConditonFromIlink.size(); i++) {
					if (!listConditonFromIlink.get(i).equals("\"" + "\"")) {
						HashMap<String, String> condition = new HashMap<>();
						condition.put("order", String.valueOf(i + 1));
						condition.put("conditionName", listConditonFromIlink.get(i).replaceAll("\"", ""));
						listCondition.add(condition);
					}
				}
			}
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listCondition, null, "Lấy danh sách điều kiện từ ilink thành công");
		} else {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, null, "Không tồn tại bản ghi nào");
		}
		return response;
	}

	@Override
	public ApiResponse updateListCondition(String programCode, String transaction, List<HashMap<String, String>> listCondition) {
		ApiResponse response;
		String key = "\"" + programCode + "#" + transaction + "\"";
		// Tìm bản ghi để update
		BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey("LKT_CHECK_CONDITIONS", key);
		if (record != null) {
			// Build danh sách các điều kiện
			StringBuilder newValue = new StringBuilder();
			for (int i = 0; i < listCondition.size(); i++) {
				String conditionName = "\"" + listCondition.get(i).get("conditionName") + "\"";
				if (listCondition.get(i).equals(listCondition.get(listCondition.size() - 1))) {
					newValue.append(conditionName);
				} else {
					newValue.append(conditionName).append(",,");
				}
			}
			record.setValue(newValue.toString());
			bstLookupTableRowRepository.saveAndFlush(record);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listCondition,
					null, "Cập nhật danh sách điều kiện thành công.");
		} else {
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listCondition,
					null, "Không tồn tại bản ghi nào phù hợp.");
		}
		return response;
	}
}
