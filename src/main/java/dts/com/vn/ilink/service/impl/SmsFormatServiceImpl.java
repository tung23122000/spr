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
import org.springframework.data.domain.Sort;
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
		response.setMessage("L???y danh s??ch t??? b???ng " + IlinkTableName.LKT_ACTIONCODE_MAPPING + " th??nh c??ng!");
		return response;
	}

	@Override
	public ApiResponse findAll(String search, Pageable pageable) {
		ApiResponse response = new ApiResponse();
		Page<BstLookupTableRow> page;
		if (StringUtils.isNotBlank(search)) {
			String keySearch = "%\"" + search.toUpperCase() + "%\"";
			page = lookupTableRowRepository.findAllWithSearch(IlinkTableName.LKT_SMS_FORMAT, keySearch, pageable);
		} else {
			page = lookupTableRowRepository.findAllWithoutSearch(IlinkTableName.LKT_SMS_FORMAT, pageable);
		}
		Page<BstLookupTableRowResponse> data = page.map(BstLookupTableRowResponse::new);
		response.setStatus(ApiResponseStatus.SUCCESS.getValue());
		response.setData(data);
		response.setMessage("L???y danh s??ch t??? b???ng " + IlinkTableName.LKT_SMS_FORMAT + " th??nh c??ng!");
		return response;
	}

	@Override
	public ApiResponse createSMS(BstLookupTableRowRequestCustom request) {
		ApiResponse response = new ApiResponse();
		// L???y tableId c???a b???ng t??? t??n b???ng
		Long tableId = lookupTableRepository.findByName(IlinkTableName.LKT_SMS_FORMAT);
		if (tableId == null) {
			throw new RuntimeException("Kh??ng t??m th???y b???ng " + IlinkTableName.LKT_PACKAGE_INFO + " tr??n database ilink");
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
					response.setMessage("C???p nh???t b???n ghi th??nh c??ng");
					return response;
				} else {
					throw new RuntimeException("B???n ghi kh??ng t???n t???i trong b???ng " + IlinkTableName.LKT_SMS_FORMAT);
				}
			} else {
				// Create
				Long rowId = lookupTableRowRepository.getMaxRowId("LKT_CHECK_CONDITIONS") + 1;
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
				response.setMessage("T???o m???i b???n ghi th??nh c??ng");
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
				response.setMessage("X??a b???n ghi th??nh c??ng");
				return response;
			} else {
				throw new RuntimeException("B???n ghi kh??ng t???n t???i trong b???ng " + IlinkTableName.LKT_SMS_FORMAT);
			}
		} else {
			response.setStatus(ApiResponseStatus.FAILED.getValue());
			response.setData(request);
			response.setMessage("Thi???u tham s??? truy???n l??n tableId ho???c rowId");
			return response;
		}
	}

	@Override
	public ApiResponse findAllLabel() {
		ApiResponse response = new ApiResponse();
		List<Label> data = labelRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		response.setStatus(ApiResponseStatus.SUCCESS.getValue());
		response.setData(data);
		response.setMessage("L???y danh s??ch label th??nh c??ng");
		return response;
	}

	@Override
	public ApiResponse saveLabels(List<Label> labels) {
		ApiResponse response = new ApiResponse();
		labelRepository.saveAll(labels);
		List<Label> data = labelRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
		response.setStatus(ApiResponseStatus.SUCCESS.getValue());
		response.setData(data);
		response.setMessage("C???p nh???t danh s??ch d??? li???u th??nh c??ng");
		return response;
	}

}