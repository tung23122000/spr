package dts.com.vn.ilink.service.impl;

import dts.com.vn.entities.Label;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.constants.IlinkTableName;
import dts.com.vn.ilink.dto.BstLookupTableRowRequestCustom;
import dts.com.vn.ilink.dto.BstLookupTableRowResponse;
import dts.com.vn.ilink.dto.Value;
import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.entities.BstLookupTableRowId;
import dts.com.vn.ilink.repository.BstLookupTableRepository;
import dts.com.vn.ilink.repository.BstLookupTableRowRepository;
import dts.com.vn.ilink.service.SmsFormatService;
import dts.com.vn.repository.LabelRepository;
import dts.com.vn.response.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SmsFormatServiceImpl implements SmsFormatService {

	private final BstLookupTableRepository lookupTableRepository;

	private final BstLookupTableRowRepository lookupTableRowRepository;

	private final LabelRepository labelRepository;

	@Autowired
	public SmsFormatServiceImpl(BstLookupTableRepository lookupTableRepository,
	                            BstLookupTableRowRepository lookupTableRowRepository,
	                            LabelRepository labelRepository) {
		this.lookupTableRepository = lookupTableRepository;
		this.lookupTableRowRepository = lookupTableRowRepository;
		this.labelRepository = labelRepository;
	}

	@Override
	public ApiResponse findAllFoFlow() {
		ApiResponse response = new ApiResponse();
		List<BstLookupTableRow> rows = lookupTableRowRepository.findByTableName(IlinkTableName.LKT_ACTIONCODE_MAPPING);
		for (BstLookupTableRow row : rows) {
			row.setKey(row.getKey().replaceAll("\"", ""));
			row.setValue(row.getValue().replaceAll("\"", ""));
		}
		response.setStatus(ApiResponseStatus.SUCCESS.getValue());
		response.setData(rows);
		response.setMessage("Lấy danh sách từ bảng " + IlinkTableName.LKT_ACTIONCODE_MAPPING + " thành công!");
		return response;
	}

	@Override
	public ApiResponse findAll(String search, Pageable pageable) {
		ApiResponse response = new ApiResponse();
		Page<BstLookupTableRow> page;
		if (StringUtils.isNotBlank(search)) {
			String keySearch = "\"" + search.toUpperCase() + "\"";
			page = lookupTableRowRepository.findAllWithSearch(IlinkTableName.LKT_SMS_FORMAT, keySearch, pageable);
		} else {
			page = lookupTableRowRepository.findAllWithoutSearch(IlinkTableName.LKT_SMS_FORMAT, pageable);
		}
		Page<BstLookupTableRowResponse> data = page.map(BstLookupTableRowResponse::new);
		response.setStatus(ApiResponseStatus.SUCCESS.getValue());
		response.setData(data);
		response.setMessage("Lấy danh sách từ bảng " + IlinkTableName.LKT_SMS_FORMAT + " thành công!");
		return response;
	}

	@Override
	public ApiResponse createSMS(BstLookupTableRowRequestCustom request) {
		ApiResponse response = new ApiResponse();
		// Lấy tableId của bảng từ tên bảng
		Long tableId = lookupTableRepository.findByName(IlinkTableName.LKT_SMS_FORMAT);
		if (tableId == null) {
			throw new RuntimeException("Không tìm thấy bảng " + IlinkTableName.LKT_PACKAGE_INFO + " trên database ilink");
		} else {
			if (request.getTableId() != null && request.getRowId() != null) {
				// Update
				BstLookupTableRowId id = new BstLookupTableRowId(request.getTableId(), request.getRowId());
				Optional<BstLookupTableRow> optRow = lookupTableRowRepository.findById(id);
				if (optRow.isPresent()) {
					BstLookupTableRow row = optRow.get();
					StringBuilder valueBuilder = new StringBuilder();
					for (Value value : request.getValues()) {
						valueBuilder.append("\"").append(value.getValue()).append("\"").append(",,");
					}
					String value = valueBuilder.substring(0, valueBuilder.length() - 2);
					row.setKey("\"" + request.getKey() + "\"");
					row.setValue(value);
					lookupTableRowRepository.saveAndFlush(row);
					response.setStatus(ApiResponseStatus.SUCCESS.getValue());
					response.setData(row);
					response.setMessage("Cập nhật bản ghi thành công");
					return response;
				} else {
					throw new RuntimeException("Bản ghi không tồn tại trong bảng " + IlinkTableName.LKT_SMS_FORMAT);
				}
			} else {
				// Create
				Long rowId = lookupTableRowRepository.getMaxRowId(tableId) + 1;
				StringBuilder valueBuilder = new StringBuilder();
				for (Value value : request.getValues()) {
					valueBuilder.append("\"").append(value.getValue()).append("\"").append(",,");
				}
				String value = valueBuilder.substring(0, valueBuilder.length() - 2);
				BstLookupTableRow row = new BstLookupTableRow();
				row.setTableId(tableId);
				row.setRowId(rowId);
				row.setKey("\"" + request.getKey() + "\"");
				row.setValue(value);
				lookupTableRowRepository.saveAndFlush(row);
				response.setStatus(ApiResponseStatus.SUCCESS.getValue());
				response.setData(row);
				response.setMessage("Tạo mới bản ghi thành công");
				return response;
			}
		}
	}

	@Override
	public ApiResponse deleteSMS(BstLookupTableRowRequestCustom request) {
		ApiResponse response = new ApiResponse();
		if (request.getTableId() != null && request.getRowId() != null) {
			BstLookupTableRowId id = new BstLookupTableRowId(request.getTableId(), request.getRowId());
			Optional<BstLookupTableRow> optRow = lookupTableRowRepository.findById(id);
			if (optRow.isPresent()) {
				BstLookupTableRow row = optRow.get();
				lookupTableRowRepository.delete(row);
				response.setStatus(ApiResponseStatus.SUCCESS.getValue());
				response.setData(null);
				response.setMessage("Xóa bản ghi thành công");
				return response;
			} else {
				throw new RuntimeException("Bản ghi không tồn tại trong bảng " + IlinkTableName.LKT_SMS_FORMAT);
			}
		} else {
			response.setStatus(ApiResponseStatus.FAILED.getValue());
			response.setData(request);
			response.setMessage("Thiếu tham số truyền lên tableId hoặc rowId");
			return response;
		}
	}

	@Override
	public ApiResponse findAllLabel() {
		ApiResponse response = new ApiResponse();
		List<Label> data = labelRepository.findAll();
		response.setStatus(ApiResponseStatus.SUCCESS.getValue());
		response.setData(data);
		response.setMessage("Lấy danh sách label thành công");
		return response;
	}

}