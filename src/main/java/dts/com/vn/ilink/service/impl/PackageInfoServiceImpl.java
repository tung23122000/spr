package dts.com.vn.ilink.service.impl;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.constants.IlinkTableName;
import dts.com.vn.ilink.dto.BstLookupTableRowRequestCustom;
import dts.com.vn.ilink.dto.BstLookupTableRowResponse;
import dts.com.vn.ilink.dto.Value;
import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.entities.BstLookupTableRowId;
import dts.com.vn.ilink.repository.BstLookupTableRepository;
import dts.com.vn.ilink.repository.BstLookupTableRowRepository;
import dts.com.vn.ilink.service.PackageInfoService;
import dts.com.vn.response.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PackageInfoServiceImpl implements PackageInfoService {

	private final BstLookupTableRepository lookupTableRepository;

	private final BstLookupTableRowRepository lookupTableRowRepository;

	@Autowired
	public PackageInfoServiceImpl(BstLookupTableRepository lookupTableRepository,
	                              BstLookupTableRowRepository lookupTableRowRepository) {
		this.lookupTableRepository = lookupTableRepository;
		this.lookupTableRowRepository = lookupTableRowRepository;
	}

	@Override
	public ApiResponse findAll(String search, Pageable pageable) {
		ApiResponse response = new ApiResponse();
		Page<BstLookupTableRow> page;
		if (StringUtils.isNotBlank(search)) {
			String keySearch = "\"" + search.toUpperCase() + "\"";
			page = lookupTableRowRepository.findAllWithSearch(IlinkTableName.LKT_PACKAGE_INFO, keySearch, pageable);
		} else {
			page = lookupTableRowRepository.findAllWithoutSearch(IlinkTableName.LKT_PACKAGE_INFO, pageable);
		}
		Page<BstLookupTableRowResponse> data = page.map(BstLookupTableRowResponse::new);
		response.setStatus(ApiResponseStatus.SUCCESS.getValue());
		response.setData(data);
		response.setMessage("L???y danh s??ch t??? b???ng " + IlinkTableName.LKT_PACKAGE_INFO + " th??nh c??ng!");
		return response;
	}

	@Override
	public ApiResponse createPackageInfo(BstLookupTableRowRequestCustom request) {
		ApiResponse response = new ApiResponse();
		// L???y tableId c???a b???ng t??? t??n b???ng
		Long tableId = lookupTableRepository.findByName(IlinkTableName.LKT_PACKAGE_INFO);
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
					throw new RuntimeException("B???n ghi kh??ng t???n t???i trong b???ng " + IlinkTableName.LKT_PACKAGE_INFO);
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
				BstLookupTableRow duplicate = lookupTableRowRepository.findByTableIdAndKey(tableId, row.getKey());
				if (duplicate != null) {
					response.setStatus(ApiResponseStatus.FAILED.getValue());
					response.setData(null);
					response.setMessage("???? t???n t???i b???n ghi v???i m?? g??i c?????c n??y");
				} else {
					lookupTableRowRepository.saveAndFlush(row);
					response.setStatus(ApiResponseStatus.SUCCESS.getValue());
					response.setData(row);
					response.setMessage("T???o m???i b???n ghi th??nh c??ng");
				}
				return response;
			}
		}
	}

	@Override
	public ApiResponse deletePackageInfo(BstLookupTableRowRequestCustom request) {
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
				throw new RuntimeException("B???n ghi kh??ng t???n t???i trong b???ng " + IlinkTableName.LKT_PACKAGE_INFO);
			}
		} else {
			response.setStatus(ApiResponseStatus.FAILED.getValue());
			response.setData(request);
			response.setMessage("Thi???u tham s??? truy???n l??n tableId ho???c rowId");
			return response;
		}
	}

	@Override
	public ApiResponse getPackageInfoByKey(String packageCode) {
		ApiResponse response = new ApiResponse();
		BstLookupTableRowResponse bstResponse = new BstLookupTableRowResponse();
		BstLookupTableRow bstLookupTableRow = lookupTableRowRepository.findByTableNameAndKey(IlinkTableName.LKT_PACKAGE_INFO,"\"" + packageCode.toUpperCase() + "\"");
		if(bstLookupTableRow!=null) {
			List<String> request = Arrays.asList(bstLookupTableRow.getValue().split(",,"));
			List<Value> newList = new ArrayList<>();
			for (int i = 0; i < request.size(); i++) {
				Value value = new Value();
				value.setValue(request.get(i).replaceAll("\"", ""));
				newList.add(value);
			}
			bstResponse.setKey(bstLookupTableRow.getKey().replaceAll("\"", ""));
			bstResponse.setRowId(bstLookupTableRow.getRowId());
			bstResponse.setTableId(bstLookupTableRow.getTableId());
			bstResponse.setValues(newList);
			response.setData(bstResponse);
			response.setStatus(ApiResponseStatus.SUCCESS.getValue());
			response.setMessage("L???y danh s??ch t??? b???ng " + IlinkTableName.LKT_PACKAGE_INFO + " th??nh c??ng!");
		}else{
			response.setStatus(ApiResponseStatus.FAILED.getValue());
			response.setData(bstResponse);
			response.setMessage("Kh??ng t???n t???i b???n ghi n??o!");
		}
		return response;
	}
}
