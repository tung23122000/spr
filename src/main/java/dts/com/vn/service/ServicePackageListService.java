package dts.com.vn.service;

import dts.com.vn.entities.BlacklistPackageList;
import dts.com.vn.entities.IsdnList;
import dts.com.vn.entities.ListDetailNew;
import dts.com.vn.entities.ServicePackageList;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.BlacklistPackageListRepository;
import dts.com.vn.repository.IsdnListRepository;
import dts.com.vn.repository.ListDetailNewRepository;
import dts.com.vn.repository.ServicePackageListRepository;
import dts.com.vn.request.MapIsdnList;
import dts.com.vn.request.MapIsdnListRequest;
import dts.com.vn.request.PackageListRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class ServicePackageListService {
    private final ServicePackageListRepository servicePackageListRepository;
    private final IsdnListService isdnListService;
    private final ListDetailNewRepository listDetailNewRepository;
    private final BlacklistPackageListRepository blacklistPackageListRepository;
    private final IsdnListRepository isdnListRepository;

    public ServicePackageListService(ServicePackageListRepository servicePackageListRepository, IsdnListService isdnListService,
                                     ListDetailNewRepository listDetailNewRepository,BlacklistPackageListRepository blacklistPackageListRepository,
                                     IsdnListRepository isdnListRepository) {
        this.servicePackageListRepository = servicePackageListRepository;
        this.isdnListService = isdnListService;
        this.listDetailNewRepository = listDetailNewRepository;
        this.blacklistPackageListRepository = blacklistPackageListRepository;
        this.isdnListRepository = isdnListRepository;
    }

    @Transactional
    public void save(PackageListRequest packageListRequest){
        if (packageListRequest.getFileName() != null && packageListRequest.getListIsdn().size() > 0) {
            //			Create isdn_list
            IsdnList isdnListRequest = new IsdnList(null, packageListRequest.getFileName(), Instant.parse(packageListRequest.getStaDate()),
                    packageListRequest.getIsdnCvCode(), packageListRequest.getIsdnDisplay(), Instant.parse(packageListRequest.getEndDate()), packageListRequest.getIsdnListType());
            IsdnList isdnListResponse = isdnListService.save(isdnListRequest);
            //			Create List_detail
            ListDetailNew listDetailNew = new ListDetailNew(null, isdnListResponse.getIsdnListId(), packageListRequest.getListIsdn());
            listDetailNewRepository.save(listDetailNew);

            //			Create ServicePackageList or BlacklistPackageList

            Instant endDate = null;
            if (packageListRequest.getEndDate() != null)
                endDate = Instant.parse(packageListRequest.getEndDate());
            if (packageListRequest.getIsdnListType().equals("0")) {
                // Create WhiteList
                ServicePackageList servicePackageList = new ServicePackageList(packageListRequest.getPackageId(), isdnListResponse.getIsdnListId(),
                        Instant.parse(packageListRequest.getStaDate()), endDate, packageListRequest.getProgramId(), null);
                servicePackageListRepository.save(servicePackageList);
            } else if (packageListRequest.getIsdnListType().equals("1")) {
                // Create Blacklist
                BlacklistPackageList blacklistPackageList = new BlacklistPackageList(null, packageListRequest.getPackageId(), packageListRequest.getProgramId(),
                        isdnListResponse.getIsdnListId(), Instant.parse(packageListRequest.getStaDate()), Instant.parse(packageListRequest.getEndDate()));
                blacklistPackageListRepository.save(blacklistPackageList);
            } else {
                throw new RestApiException(ErrorCode.DATA_FAILED);
            }
        }
    }

    public void mapIsdnList(MapIsdnListRequest request) {
        List<MapIsdnList> list = request.getListMapIsdn();
        for (MapIsdnList item: list) {
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
}
