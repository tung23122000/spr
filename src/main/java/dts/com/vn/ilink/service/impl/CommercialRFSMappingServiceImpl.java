package dts.com.vn.ilink.service.impl;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.constants.IlinkTableName;
import dts.com.vn.ilink.dto.BstLookupTableRowRequest;
import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.entities.BstLookupTableRowId;
import dts.com.vn.ilink.entities.CommercialMapping;
import dts.com.vn.ilink.repository.BstLookupTableRepository;
import dts.com.vn.ilink.repository.BstLookupTableRowRepository;
import dts.com.vn.ilink.service.CommercialRFSMappingService;
import dts.com.vn.response.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommercialRFSMappingServiceImpl implements CommercialRFSMappingService {

	private final BstLookupTableRepository lookupTableRepository;

	private final BstLookupTableRowRepository repository;

	@Autowired
	public CommercialRFSMappingServiceImpl(BstLookupTableRepository lookupTableRepository,
	                                       BstLookupTableRowRepository repository) {
		this.lookupTableRepository = lookupTableRepository;
		this.repository = repository;
	}

	@Override
	public ApiResponse findAll(String search, Pageable pageable) {
		ApiResponse response = new ApiResponse();
		Page<BstLookupTableRow> page;
		if (StringUtils.isNotBlank(search)) {
			String keySearch = "\"" + search.toUpperCase() + "\"";
			page = repository.findAllWithSearch(IlinkTableName.LKT_COMMERCIAL_RFS_MAPPING, keySearch, pageable);
		} else {
			page = repository.findAllWithoutSearch(IlinkTableName.LKT_COMMERCIAL_RFS_MAPPING, pageable);
		}
		Page<CommercialMapping> data = page.map(CommercialMapping::new);
		response.setStatus(ApiResponseStatus.SUCCESS.getValue());
		response.setData(data);
		response.setMessage("Lấy thông tin mapping gói cước với luồng thành công");
		return response;
	}

	@Override
	public ApiResponse createMapping(BstLookupTableRowRequest request) {
		ApiResponse response = new ApiResponse();
		// Lấy tableId của bảng từ tên bảng
		Long tableId = lookupTableRepository.findByName(IlinkTableName.LKT_COMMERCIAL_RFS_MAPPING);
		if (tableId == null) {
			throw new RuntimeException("Không tìm thấy bảng " + IlinkTableName.LKT_COMMERCIAL_RFS_MAPPING + " trên database ilink");
		} else {
			if (request.getTableId() != null && request.getRowId() != null) {
				// Update
				BstLookupTableRowId id = new BstLookupTableRowId(request.getTableId(), request.getRowId());
				Optional<BstLookupTableRow> optRow = repository.findById(id);
				if (optRow.isPresent()) {
					BstLookupTableRow row = optRow.get();
					row.setKey("\"" + request.getKey() + "\"");
					row.setValue("\"" + request.getValue() + "\"");
					repository.saveAndFlush(row);
					response.setStatus(ApiResponseStatus.SUCCESS.getValue());
					response.setData(row);
					response.setMessage("Cập nhật bản ghi thành công");
					return response;
				} else {
					throw new RuntimeException("Bản ghi không tồn tại trong bảng " + IlinkTableName.LKT_COMMERCIAL_RFS_MAPPING);
				}
			} else {
				// Create
				Long rowId = repository.getMaxRowId(tableId) + 1;
				BstLookupTableRow row = new BstLookupTableRow();
				row.setTableId(tableId);
				row.setRowId(rowId);
				row.setKey("\"" + request.getKey() + "\"");
				row.setValue("\"" + request.getValue() + "\"");
				repository.saveAndFlush(row);
				response.setStatus(ApiResponseStatus.SUCCESS.getValue());
				response.setData(row);
				response.setMessage("Tạo mới bản ghi thành công");
				return response;
			}
		}
	}

	@Override
	public ApiResponse deleteMapping(CommercialMapping request) {
		ApiResponse response = new ApiResponse();
		if (request.getTableId() != null && request.getRowId() != null) {
			BstLookupTableRowId id = new BstLookupTableRowId(request.getTableId(), request.getRowId());
			Optional<BstLookupTableRow> optRow = repository.findById(id);
			if (optRow.isPresent()) {
				BstLookupTableRow row = optRow.get();
				repository.delete(row);
				response.setStatus(ApiResponseStatus.SUCCESS.getValue());
				response.setData(null);
				response.setMessage("Xóa bản ghi thành công");
				return response;
			} else {
				throw new RuntimeException("Bản ghi không tồn tại trong bảng " + IlinkTableName.LKT_COMMERCIAL_RFS_MAPPING);
			}
		} else {
			response.setStatus(ApiResponseStatus.FAILED.getValue());
			response.setData(request);
			response.setMessage("Thiếu tham số truyền lên tableId hoặc rowId");
			return response;
		}
	}

}
