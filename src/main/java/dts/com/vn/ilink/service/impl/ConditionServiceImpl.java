package dts.com.vn.ilink.service.impl;

import dts.com.vn.entities.ListCondition;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.constants.IlinkTableName;
import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.entities.Condition;
import dts.com.vn.ilink.repository.BstLookupTableRowRepository;
import dts.com.vn.ilink.service.ConditionService;
import dts.com.vn.repository.ListConditionRepository;
import dts.com.vn.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
	 * Description - Lấy danh sách các điều kiện để config trong bảng list_condition
	 *
	 * @return any
	 * @author - giangdh
	 * @created - 11/12/2021
	 */
	@Override
	public ApiResponse findAllCondition() {
		ApiResponse response;
		try {
			List<ListCondition> listConditions = listConditionRepository.findAll();
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listConditions, null, "Lấy danh sách điều kiện thành công");
		} catch (Exception e) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), null, null, "Lấy danh sách điều kiện thất bại");
		}
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
		BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey(IlinkTableName.LKT_CHECK_CONDITIONS, key);
		if (record != null) {
			List<HashMap<String, String>> listCondition = new ArrayList<>();
			if (record.getValue() != null) {
				List<String> listConditonFromIlink = new ArrayList<>(Arrays.asList(record.getValue().split(",,")));
				for (int i = 0; i < listConditonFromIlink.size(); i++) {
					if (!listConditonFromIlink.get(i).equals("\"" + "\"")) {
						HashMap<String, String> condition = new HashMap<>();
						String[] value = listConditonFromIlink.get(i).replaceAll("\"", "").split("#");
						condition.put("conditionName", value[1].trim());
						condition.put("ilinkServiceName", value[0].trim());
						condition.put("order", String.valueOf(i + 1));
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

	/**
	 * Description - Tạo mới danh sách điều kiện cho 1 CT vs 1 luồng
	 *
	 * @param programCode   - Mã chương trình
	 * @param transaction   - Luồng
	 * @param listCondition - Danh sách điều kiện
	 * @return any
	 * @author - giangdh
	 * @created - 11/16/2021
	 */
	@Override
	public ApiResponse createListCondition(String programCode, String transaction, List<Condition> listCondition) {
		ApiResponse response;
		String key = "\"" + programCode + "#" + transaction + "\"";
		// Tìm xem có bản ghi trùng không
		BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey("LKT_CHECK_CONDITIONS", key);
		if (record != null) {
			response = new ApiResponse(ApiResponseStatus.FAILED.getValue(), record,
					null, "Đã tồn tại bản ghi với chương trình và luồng này");
		} else {
			// Build danh sách các điều kiện
			StringBuilder newValue = new StringBuilder();
			// Sắp sếp lại danh sách điều kiện theo thứ tự dựa vào order
			listCondition.sort(Comparator.comparing(Condition::getOrder));
			for (int i = 0; i < listCondition.size(); i++) {
				String conditionName = "\"" + listCondition.get(i).getConditionName() + "\"";
				if (listCondition.get(i).equals(listCondition.get(listCondition.size() - 1))) {
					newValue.append(conditionName);
				} else {
					newValue.append(conditionName).append(",,");
				}
			}
			// Tạo mới 1 record
			BstLookupTableRow row = new BstLookupTableRow();
			Long rowId = bstLookupTableRowRepository.getMaxRowId(702) + 1;
			row.setTableId(702L);
			row.setRowId(rowId);
			row.setKey(key);
			row.setValue(newValue.toString());
			bstLookupTableRowRepository.saveAndFlush(row);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), row,
					null, "Thêm mới danh sách điều kiện thành công");
		}
		return response;
	}

	/**
	 * Description - Cập nhật danh sách điều kiện cho 1 CT vs 1 luồng
	 *
	 * @param programCode   - Mã chương trình
	 * @param transaction   - Luồng
	 * @param listCondition - Danh sách điều kiện
	 * @return any
	 * @author - giangdh
	 * @created - 11/16/2021
	 */
	@Override
	public ApiResponse updateListCondition(String programCode, String transaction, List<Condition> listCondition) {
		ApiResponse response;
		String key = "\"" + programCode + "#" + transaction + "\"";
		// Tìm bản ghi để update
		BstLookupTableRow record = bstLookupTableRowRepository.findByTableNameAndKey("LKT_CHECK_CONDITIONS", key);
		if (record != null) {
			// Build danh sách các điều kiện
			StringBuilder newValue = new StringBuilder();
			// Sắp sếp lại danh sách điều kiện theo thứ tự dựa vào order
			listCondition.sort(Comparator.comparing(Condition::getOrder));
			for (int i = 0; i < listCondition.size(); i++) {
				String conditionName = "\"" + listCondition.get(i).getConditionName() + "\"";
				if (listCondition.get(i).equals(listCondition.get(listCondition.size() - 1))) {
					newValue.append(conditionName);
				} else {
					newValue.append(conditionName).append(",,");
				}
			}
			record.setValue(newValue.toString());
			bstLookupTableRowRepository.saveAndFlush(record);
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), record,
					null, "Cập nhật danh sách điều kiện thành công.");
		} else {
			response = new ApiResponse(ApiResponseStatus.SUCCESS.getValue(), listCondition,
					null, "Không tồn tại bản ghi nào phù hợp.");
		}
		return response;
	}

	/**
	 * Description - Xóa danh sách điều kiện cho 1 CT vs 1 luồng
	 *
	 * @param programCode - Mã chương trình
	 * @param transaction - Luồng
	 * @return any
	 * @author - giangdh
	 * @created - 11/16/2021
	 */
	@Override
	public ApiResponse deleteListCondition(String programCode, String transaction) {
		return null;
	}
}
