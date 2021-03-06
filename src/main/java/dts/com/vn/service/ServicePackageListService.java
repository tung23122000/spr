package dts.com.vn.service;

import dts.com.vn.entities.BlacklistPackageList;
import dts.com.vn.entities.IsdnList;
import dts.com.vn.entities.ListDetailNew;
import dts.com.vn.entities.ServicePackageList;
import dts.com.vn.enumeration.ApiResponseStatus;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.properties.AppConfigProperties;
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ServicePackageListService {

    private final ServicePackageListRepository servicePackageListRepository;

    private final AppConfigProperties appConfigProperties;

    private final IsdnListService isdnListService;

    private final ListDetailNewRepository listDetailNewRepository;

    private final BlacklistPackageListRepository blacklistPackageListRepository;

    private final IsdnListRepository isdnListRepository;

    @Autowired
    public ServicePackageListService(ServicePackageListRepository servicePackageListRepository,
                                     AppConfigProperties appConfigProperties,
                                     IsdnListService isdnListService,
                                     ListDetailNewRepository listDetailNewRepository,
                                     BlacklistPackageListRepository blacklistPackageListRepository,
                                     IsdnListRepository isdnListRepository) {
        this.servicePackageListRepository = servicePackageListRepository;
        this.appConfigProperties = appConfigProperties;
        this.isdnListService = isdnListService;
        this.listDetailNewRepository = listDetailNewRepository;
        this.blacklistPackageListRepository = blacklistPackageListRepository;
        this.isdnListRepository = isdnListRepository;
    }

    @Transactional
    public void save(PackageListRequest packageListRequest) throws InterruptedException {
        ApiResponse response= new ApiResponse();
        if (packageListRequest.getFileName() != null && packageListRequest.getListIsdn().size() != 0) {
            //	T???o danh s??ch ?????i t?????ng
            IsdnList isdnListRequest;
            if (packageListRequest.getStaDate() != null && packageListRequest.getEndDate() != null) {
                isdnListRequest = new IsdnList(null, packageListRequest.getFileName(), Instant.parse(packageListRequest.getStaDate()),
                        packageListRequest.getIsdnCvCode(), packageListRequest.getIsdnDisplay(), Instant.parse(packageListRequest.getEndDate()), packageListRequest.getIsdnListType());
            } else {
                isdnListRequest = new IsdnList(null, packageListRequest.getFileName(), Instant.parse(packageListRequest.getStaDate()),
                        packageListRequest.getIsdnCvCode(), packageListRequest.getIsdnDisplay(), null, packageListRequest.getIsdnListType());
            }
            IsdnList isdnListResponse = isdnListService.save(isdnListRequest);
            if (packageListRequest.getListIsdn().size() > 1000000) {
                int n = packageListRequest.getListIsdn().size() / 1000000;
                AtomicInteger counter = new AtomicInteger();
                Collection<List<String>> result = packageListRequest.getListIsdn().stream()
                        .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 1000000))
                        .values();
                ExecutorService executorService = Executors.newFixedThreadPool(n + 1);
                List<String> listIsdnFail = new ArrayList<>();
                List<Callable<ListDetailNew>> callables = new ArrayList<>();
                for (List<String> item : result) {
                    callables.add(() -> {
                        ListDetailNew listDetailNew = null;
                        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
                        for (String s : item) {
                            if (s.replaceAll("\r", "").length() == 9||s.replaceAll("\r", "").length() == 10) {
                                map.put(s.replaceAll("\r", ""), 1);
                            }else {
                                listIsdnFail.add(s.replaceAll("\r", ""));
                            }
                        }
                        if(map.size()>0){
                            listDetailNew = new ListDetailNew(null, isdnListResponse.getIsdnListId(), map, packageListRequest.getIsdnDisplay());
                            listDetailNewRepository.save(listDetailNew);
                        }
                        return listDetailNew;
                    });
                }
                executorService.invokeAll(callables);
                writeLogIsdnFail(listIsdnFail,isdnListResponse.getIsdnListId());
            } else {
                ListDetailNew listDetailNew;
                List<String> listIsdnFail = new ArrayList<>();
                LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
                for (int i = 0; i < packageListRequest.getListIsdn().size(); i++) {
                    if(packageListRequest.getListIsdn().get(i).replaceAll("\r", "").length() == 9||packageListRequest.getListIsdn().get(i).replaceAll("\r", "").length() == 10){
                        map.put(packageListRequest.getListIsdn().get(i).replaceAll("\r", ""), 1);
                    }else {
                        listIsdnFail.add(packageListRequest.getListIsdn().get(i).replaceAll("\r", ""));
                    }
                }
                //	T???o danh s??ch chi ti???t
                if(map.size()>0){
                    listDetailNew = new ListDetailNew(null, isdnListResponse.getIsdnListId(), map, packageListRequest.getIsdnDisplay());
                    listDetailNewRepository.save(listDetailNew);
                }
                writeLogIsdnFail(listIsdnFail,isdnListResponse.getIsdnListId());
            }
            //T???o danh s??ch whitelist or blacklist
            Instant endDate = null;
            if (packageListRequest.getEndDate() != null) endDate = Instant.parse(packageListRequest.getEndDate()).with(ChronoField.NANO_OF_SECOND, 0);
            if (packageListRequest.getIsdnListType().equals("0")) {
                // T???o WhiteList
                ServicePackageList servicePackageList = new ServicePackageList(packageListRequest.getPackageId(), isdnListResponse.getIsdnListId(),
                        Instant.parse(packageListRequest.getStaDate()).with(ChronoField.NANO_OF_SECOND, 0), endDate, packageListRequest.getProgramId(), null);
                servicePackageListRepository.save(servicePackageList);
            } else if (packageListRequest.getIsdnListType().equals("1")) {
                //T???o Blacklist
                if (packageListRequest.getEndDate() != null) {
                    BlacklistPackageList blacklistPackageList = new BlacklistPackageList(null, packageListRequest.getPackageId(), packageListRequest.getProgramId(),
                            isdnListResponse.getIsdnListId(), Instant.parse(packageListRequest.getStaDate()).with(ChronoField.NANO_OF_SECOND, 0), Instant.parse(packageListRequest.getEndDate()).with(ChronoField.NANO_OF_SECOND, 0));
                    blacklistPackageListRepository.save(blacklistPackageList);
                } else {
                    BlacklistPackageList blacklistPackageList = new BlacklistPackageList(null, packageListRequest.getPackageId(), packageListRequest.getProgramId(),
                            isdnListResponse.getIsdnListId(), Instant.parse(packageListRequest.getStaDate()).with(ChronoField.NANO_OF_SECOND, 0), null);
                    blacklistPackageListRepository.save(blacklistPackageList);
                }
            }
        } else {
            response.setMessage("D??? li???u truy???n v??o sai!");
            throw new RestApiException(ErrorCode.DATA_FAILED);
        }
    }

    private void writeLogIsdnFail(List<String> listIsdnFail, Long isdnListId)  {
        try{
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
            String fileName = "/home/spr/failed-folder/" +isdnListId+"_"+ timeStamp+"_"+
                    ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            for (String s : listIsdnFail) {
                writer.write(s+"\r");
            }
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
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
        // 1. X??a danh s??ch ?????i t?????ng
        Optional<IsdnList> optIsdnList = isdnListRepository.findById(listId);
        if (optIsdnList.isPresent()) {
            isdnListRepository.delete(optIsdnList.get());
            if (listType == 0) {
                servicePackageListRepository.deleteByIsdnListId(listId);
            }
            // 2. X??a list detail new, ?????ng th???i x??a ??? whitelist ho???c blacklist
            listDetailNewRepository.deleteByIsdnListId(listId);
            response.setStatus(ApiResponseStatus.SUCCESS.getValue());
            response.setData(null);
            response.setMessage("X??a d??? li???u th??nh c??ng");
            return response;
        } else {
            throw new RuntimeException("B???n ghi kh??ng t???n t???i");
        }
    }

}
