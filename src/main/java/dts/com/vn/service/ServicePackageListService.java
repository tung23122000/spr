package dts.com.vn.service;

import dts.com.vn.entities.IsdnList;
import dts.com.vn.entities.ListDetailNew;
import dts.com.vn.entities.ServicePackageList;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.repository.ListDetailNewRepository;
import dts.com.vn.repository.ServicePackageListRepository;
import dts.com.vn.request.ServicePackageListRequest;
import dts.com.vn.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class ServicePackageListService {
    private final ServicePackageListRepository servicePackageListRepository;
    private final IsdnListService isdnListService;
    private final ListDetailNewRepository listDetailNewRepository;

    public ServicePackageListService(ServicePackageListRepository servicePackageListRepository, IsdnListService isdnListService,
                                     ListDetailNewRepository listDetailNewRepository) {
        this.servicePackageListRepository = servicePackageListRepository;
        this.isdnListService = isdnListService;
        this.listDetailNewRepository = listDetailNewRepository;
    }

    public void save(ServicePackageListRequest servicePackageListRequest){
        if (servicePackageListRequest.getFileName() != null && servicePackageListRequest.getListIsdn().size() > 0) {
            //			Create isdn_list
            IsdnList isdnListRequest = new IsdnList(null, servicePackageListRequest.getFileName(), Instant.now(), null, "1", null, "0");
            IsdnList isdnListResponse = isdnListService.save(isdnListRequest);
            //			Create List_detail
            ListDetailNew listDetailNew = new ListDetailNew(null, isdnListResponse.getIsdnListId(), servicePackageListRequest.getListIsdn());
            listDetailNewRepository.save(listDetailNew);
            //			Create ServicePackageList
            Instant endDate = null;
            if (servicePackageListRequest.getEndDate() != null)
                endDate = Instant.parse(servicePackageListRequest.getEndDate());
            ServicePackageList servicePackageList = new ServicePackageList(servicePackageListRequest.getPackageId(), isdnListResponse.getIsdnListId(),
                    Instant.parse(servicePackageListRequest.getStaDate()), endDate, servicePackageListRequest.getProgramId(), null);
            servicePackageListRepository.save(servicePackageList);
        }
    }
}
