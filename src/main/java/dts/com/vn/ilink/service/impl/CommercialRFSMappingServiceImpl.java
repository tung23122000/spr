package dts.com.vn.ilink.service.impl;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.entities.CommercialMapping;
import dts.com.vn.ilink.repository.BstLookupTableRowRepository;
import dts.com.vn.ilink.service.CommercialRFSMappingService;
import dts.com.vn.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
		List<CommercialMapping> data = new ArrayList<>();
		for (BstLookupTableRow row : page) {
			CommercialMapping item = new CommercialMapping();
			item.setTableId(row.getTableId());
			item.setRowId(row.getRowId());
			item.setCommercialPackageCode(row.getKey().replaceAll("\"", ""));
			item.setRfsMapping(row.getValue().replaceAll("\"", ""));
			data.add(item);
		}
		response.setStatus(ApiResponseStatus.SUCCESS.getValue());
		response.setData(data);
		response.setMessage("Lấy thông tin mapping gói cước với luồng thành công");
		return response;
	}

	@Override
	public ApiResponse createMapping(CommercialMapping request) {
		return null;
	}

	@Override
	public ApiResponse deleteMapping(CommercialMapping request) {
		return null;
	}
}
