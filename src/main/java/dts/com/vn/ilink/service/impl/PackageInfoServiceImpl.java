package dts.com.vn.ilink.service.impl;

import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.ilink.constants.IlinkTableName;
import dts.com.vn.ilink.dto.BstLookupTableRowResponse;
import dts.com.vn.ilink.dto.CommercialMappingRequest;
import dts.com.vn.ilink.entities.BstLookupTableRow;
import dts.com.vn.ilink.entities.CommercialMapping;
import dts.com.vn.ilink.repository.BstLookupTableRepository;
import dts.com.vn.ilink.repository.BstLookupTableRowRepository;
import dts.com.vn.ilink.service.PackageInfoService;
import dts.com.vn.response.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
		response.setMessage("Lấy danh sách từ bảng " + IlinkTableName.LKT_PACKAGE_INFO + " thành công!");
		return response;
	}

	@Override
	public ApiResponse createMapping(CommercialMappingRequest request) {
		return null;
	}

	@Override
	public ApiResponse deleteMapping(CommercialMapping request) {
		return null;
	}
}
