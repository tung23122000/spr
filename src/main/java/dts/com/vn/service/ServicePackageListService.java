package dts.com.vn.service;

import dts.com.vn.entities.BlacklistPackageList;
import dts.com.vn.entities.IsdnList;
import dts.com.vn.entities.ListDetailNew;
import dts.com.vn.entities.ServicePackageList;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.BlacklistPackageListRepository;
import dts.com.vn.repository.IsdnListRepository;
import dts.com.vn.repository.ListDetailNewRepository;
import dts.com.vn.repository.ServicePackageListRepository;
import dts.com.vn.request.MapIsdnList;
import dts.com.vn.request.MapIsdnListRequest;
import dts.com.vn.request.PackageListRequest;
import dts.com.vn.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ServicePackageListService {

	private final ServicePackageListRepository servicePackageListRepository;

	private final IsdnListService isdnListService;

	private final ListDetailNewRepository listDetailNewRepository;

	private final BlacklistPackageListRepository blacklistPackageListRepository;

	private final IsdnListRepository isdnListRepository;

	@Autowired
	public ServicePackageListService(ServicePackageListRepository servicePackageListRepository, IsdnListService isdnListService,
	                                 ListDetailNewRepository listDetailNewRepository, BlacklistPackageListRepository blacklistPackageListRepository,
	                                 IsdnListRepository isdnListRepository) {
		this.servicePackageListRepository = servicePackageListRepository;
		this.isdnListService = isdnListService;
		this.listDetailNewRepository = listDetailNewRepository;
		this.blacklistPackageListRepository = blacklistPackageListRepository;
		this.isdnListRepository = isdnListRepository;
	}

	@Transactional
	public void save(PackageListRequest packageListRequest) {
		if (packageListRequest.getFileName() != null && packageListRequest.getListIsdn().size()!=0) {
			//	Tạo danh sách đối tượng
			IsdnList isdnListRequest;
			if (packageListRequest.getStaDate() != null && packageListRequest.getEndDate() != null) {
				isdnListRequest = new IsdnList(null, packageListRequest.getFileName(), Instant.parse(packageListRequest.getStaDate()),
						packageListRequest.getIsdnCvCode(), packageListRequest.getIsdnDisplay(), Instant.parse(packageListRequest.getEndDate()), packageListRequest.getIsdnListType());
			} else {
				isdnListRequest = new IsdnList(null, packageListRequest.getFileName(), Instant.parse(packageListRequest.getStaDate()),
						packageListRequest.getIsdnCvCode(), packageListRequest.getIsdnDisplay(), null, packageListRequest.getIsdnListType());
			}
			LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
			for (int i = 0; i < packageListRequest.getListIsdn().size(); i++) {
				map.put(packageListRequest.getListIsdn().get(i).replaceAll("\r",""),1);
			}
			IsdnList isdnListResponse = isdnListService.save(isdnListRequest);
            //	Tạo danh sách chi tiết
            ListDetailNew listDetailNew = new ListDetailNew(null, isdnListResponse.getIsdnListId(), map, packageListRequest.getIsdnDisplay());
            listDetailNewRepository.save(listDetailNew);
           // Tạo danh sách whitelist or blacklist
            Instant endDate = null;
            if (packageListRequest.getEndDate() != null) endDate = Instant.parse(packageListRequest.getEndDate());
            if (packageListRequest.getIsdnListType().equals("0")) {
                // Tạo WhiteList
                ServicePackageList servicePackageList = new ServicePackageList(packageListRequest.getPackageId(), isdnListResponse.getIsdnListId(),
                        Instant.parse(packageListRequest.getStaDate()), endDate, packageListRequest.getProgramId(), null);
                servicePackageListRepository.save(servicePackageList);
            } else if (packageListRequest.getIsdnListType().equals("1")) {
                //Tạo Blacklist
                if (packageListRequest.getEndDate() != null) {
                    BlacklistPackageList blacklistPackageList = new BlacklistPackageList(null, packageListRequest.getPackageId(), packageListRequest.getProgramId(),
                            isdnListResponse.getIsdnListId(), Instant.parse(packageListRequest.getStaDate()), Instant.parse(packageListRequest.getEndDate()));
                    blacklistPackageListRepository.save(blacklistPackageList);
                }else {
                    BlacklistPackageList blacklistPackageList = new BlacklistPackageList(null, packageListRequest.getPackageId(), packageListRequest.getProgramId(),
                            isdnListResponse.getIsdnListId(), Instant.parse(packageListRequest.getStaDate()), null);
                    blacklistPackageListRepository.save(blacklistPackageList);
                }
			} else {
				throw new RestApiException(ErrorCode.DATA_FAILED);
			}
		}
	}

	public void mapIsdnList(MapIsdnListRequest request) {
		List<MapIsdnList> list = request.getListMapIsdn();
		for (MapIsdnList item : list) {
			IsdnList isdnList = isdnListRepository.findById(item.getIsdnListId()).orElse(null);
			if (isdnList.getListType().equals("0")) {
				// Create WhiteList
				ServicePackageList servicePackageList = new ServicePackageList(request.getPackageId(), isdnList.getIsdnListId(),
						isdnList.getCreateDate(), isdnList.getEndDate(), request.getProgramId(), null);
				servicePackageListRepository.save(servicePackageList);
			} else if (isdnList.getListType().equals("1")) {
				// Create Blacklist
				BlacklistPackageList blacklistPackageList = new BlacklistPackageList(null, request.getPackageId(), request.getProgramId(),
						isdnList.getIsdnListId(), isdnList.getCreateDate(), isdnList.getEndDate());
				blacklistPackageListRepository.save(blacklistPackageList);
			} else {
				throw new RestApiException(ErrorCode.DATA_FAILED);
			}
		}
	}

	public List<IsdnList> getWhiteListByProgramId(Long programId) {
		return isdnListRepository.getWhiteListByProgramId(programId);
	}

	public List<IsdnList> getBlackListByProgramId(Long programId) {
		return isdnListRepository.getBlackListByProgramId(programId);
	}

	@Transactional
	public ApiResponse deleteServicePackageList(Long listId, Integer listType) {
		ApiResponse response = new ApiResponse();
		// 1. Xóa danh sách đối tượng
		Optional<IsdnList> optIsdnList = isdnListRepository.findById(listId);
		if (optIsdnList.isPresent()) {
			isdnListRepository.delete(optIsdnList.get());
			if(listType==0){
				servicePackageListRepository.deleteByIsdnListId(listId);
			}
			// 2. Xóa list detail new, đồng thời xóa ở whitelist hoặc blacklist
			listDetailNewRepository.deleteByIsdnListId(listId);
			response.setStatus(ApiResponseStatus.SUCCESS.getValue());
			response.setData(null);
			response.setMessage("Xóa dữ liệu thành công");
			return response;
		} else {
			throw new RuntimeException("Bản ghi không tồn tại");
		}
	}

}
