package dts.com.vn.service;

import dts.com.vn.entities.IsdnList;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.properties.AppConfigProperties;
import dts.com.vn.repository.BlacklistPackageListRepository;
import dts.com.vn.repository.IsdnListRepository;
import dts.com.vn.repository.ListDetailNewRepository;
import dts.com.vn.repository.ServicePackageListRepository;
import dts.com.vn.request.IsdnListRequest;
import dts.com.vn.util.DateTimeUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.text.ParseException;
import java.util.List;

@Service
public class IsdnListService {

    private final IsdnListRepository isdnListRepository;

    private final AppConfigProperties appConfigProperties;

    private final ServicePackageListRepository servicePackageListRepository;

    private final BlacklistPackageListRepository blacklistPackageListRepository;

    private final ListDetailNewRepository listDetailNewRepository;

    public IsdnListService(IsdnListRepository isdnListRepository, AppConfigProperties appConfigProperties, ServicePackageListRepository servicePackageListRepository, BlacklistPackageListRepository blacklistPackageListRepository, ListDetailNewRepository listDetailNewRepository) {
        this.isdnListRepository = isdnListRepository;
        this.appConfigProperties = appConfigProperties;
        this.servicePackageListRepository = servicePackageListRepository;
        this.blacklistPackageListRepository = blacklistPackageListRepository;
        this.listDetailNewRepository = listDetailNewRepository;
    }

    public IsdnList save(IsdnList isdnList) {
        return isdnListRepository.save(isdnList);
    }

    public IsdnList saveIsdnList(IsdnListRequest request) throws ParseException {
        // Validate
        if (request.getListType() == null || request.getName() == null || request.getCvCodeList() == null || request.getCreateDate() == null) {
            throw new RestApiException(ErrorCode.MISSING_DATA_FIELD);
        }
        if (request.getCvCodeList() != null) {
            // Số lần xuất hiện của dấu phẩy
            int occurance = StringUtils.countOccurrencesOf(request.getCvCodeList(), ",");
            String arr[] = request.getCvCodeList().split(",");
            if (arr.length == occurance) {
                throw new RestApiException(ErrorCode.VALIDATE_FAIL);
            } else {
                for (String item : arr) {
                    if (item.trim().equals("")) {
                        throw new RestApiException(ErrorCode.VALIDATE_FAIL);
                    }
                }
            }
        }
        if (request.getListType().equals("0")) {
            // Cập nhật WhiteList
            if (request.getEndDate() != null) {
                servicePackageListRepository.updateWhiteListByIsdnListId(request.getIsdnListId(), DateTimeUtil.convertStringToInstant(request.getCreateDate(), "dd/MM/yyyy HH:mm:ss"),DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss"));
            } else {
                servicePackageListRepository.updateWhiteListByIsdnListIdByStaDate(request.getIsdnListId(), DateTimeUtil.convertStringToInstant(request.getCreateDate(), "dd/MM/yyyy HH:mm:ss"));
            }
        } else {
            //Cập nhật Blacklist
            if (request.getEndDate() != null) {
                blacklistPackageListRepository.updateBlackListByIsdnListId(request.getIsdnListId(),DateTimeUtil.convertStringToInstant(request.getCreateDate(), "dd/MM/yyyy HH:mm:ss"),DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss"));
            } else {
                blacklistPackageListRepository.updateBlackListByIsdnListIdAndStaDate(request.getIsdnListId(), DateTimeUtil.convertStringToInstant(request.getCreateDate(), "dd/MM/yyyy HH:mm:ss"));
            }
        }
        IsdnList isdnList = new IsdnList();
        if (request.getIsdnListId() != null) {
            isdnList.setIsdnListId(request.getIsdnListId());
        }
        listDetailNewRepository.updateListDetailNewStatus(request.getIsdnListId(), request.getIsDisplay());
        isdnList.setName(request.getName());
        isdnList.setCvCodeList(request.getCvCodeList());
        isdnList.setIsDisplay(request.getIsDisplay());
        isdnList.setListType(request.getListType());
        isdnList.setCreateDate(DateTimeUtil.convertStringToInstant(request.getCreateDate(), "dd/MM/yyyy HH:mm:ss"));
        isdnList.setEndDate(DateTimeUtil.convertStringToInstant(request.getEndDate(), "dd/MM/yyyy HH:mm:ss"));
        isdnList = isdnListRepository.save(isdnList);

        // Sau khi lưu, tự động tạo ra một thư mục với tên thư mục chính là ID của danh sách tại vị trí: /home/spr/import: Ví dụ: /home/spr/import/3544
        // 10/10/2021
        String path = appConfigProperties.getAutoImport() + isdnList.getIsdnListId();
        File createFolder = new File(path);
        if (!createFolder.getParentFile().exists()) {
            createFolder.getParentFile().mkdir();
        }
        if (!createFolder.exists()) {
            createFolder.mkdir();
        }
        return isdnList;
    }

    public Page<IsdnList> findAll(Pageable pageable) {
        return isdnListRepository.findAll(pageable);
    }

    public List<IsdnList> findAllNotMapped() {
        return isdnListRepository.findAllNotMapped();
    }
}
