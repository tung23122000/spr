package dts.com.vn.ilink.service;

import dts.com.vn.ilink.entities.CommercialMapping;
import dts.com.vn.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface CommercialRFSMappingService {

	ApiResponse findAll(String tableName, Pageable pageable);

	ApiResponse createMapping(CommercialMapping request);

	ApiResponse deleteMapping(CommercialMapping request);

}
