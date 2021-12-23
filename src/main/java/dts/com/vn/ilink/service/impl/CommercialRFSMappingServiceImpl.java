package dts.com.vn.ilink.service.impl;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.constants.IlinkTableName;
import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.entities.BstLookupTableRowId;
import dts.com.vn.ilink.entities.CommercialMapping;
import dts.com.vn.ilink.repository.BstLookupTableRowRepository;
import dts.com.vn.ilink.service.CommercialRFSMappingService;
import dts.com.vn.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommercialRFSMappingServiceImpl implements CommercialRFSMappingService {

	private final BstLookupTableRowRepository repository;

	@Autowired
	public CommercialRFSMappingServiceImpl(BstLookupTableRowRepository repository) {
		this.repository = repository;
	}

	@Override
	public ApiResponse findAll(String tableName, Pageable pageable) {
		ApiResponse response = new ApiResponse();
		Page<BstLookupTableRow> page = repository.findAll(tableName, pageable);
		Page<CommercialMapping> data = page.map(CommercialMapping::new);
		response.setStatus(ApiResponseStatus.SUCCESS.getValue());
		response.setData(data);
		response.setMessage("Lấy thông tin mapping gói cước với luồng thành công");
		return response;
	}

	@Override
	public ApiResponse createMapping(CommercialMapping request) {
		ApiResponse response = new ApiResponse();
		if (request.getTableId() != null && request.getRowId() != null) {
			BstLookupTableRowId id = new BstLookupTableRowId(request.getTableId(), request.getRowId());
			Optional<BstLookupTableRow> optRow = repository.findById(id);
			if (optRow.isPresent()) {
				// Update
				BstLookupTableRow row = optRow.get();
				row.setKey("\"" + request.getCommercialPackageCode() + "\"");
				row.setValue("\"" + request.getRfsMapping() + "\"");
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
			Long rowId = repository.getMaxRowId(703) + 1;
			request.setRowId(rowId);
			request.setCommercialPackageCode("\"" + request.getCommercialPackageCode() + "\"");
			request.setRfsMapping("\"" + request.getRfsMapping() + "\"");
			BstLookupTableRow row = new BstLookupTableRow(request);
			repository.saveAndFlush(row);
			response.setStatus(ApiResponseStatus.SUCCESS.getValue());
			response.setData(row);
			response.setMessage("Tạo mới bản ghi thành công");
			return response;
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
