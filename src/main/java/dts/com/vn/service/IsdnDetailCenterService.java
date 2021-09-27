package dts.com.vn.service;

import dts.com.vn.entities.IsdnDetailCenter;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceType;
import dts.com.vn.entities.Services;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.IsdnDetailCenterRepository;
import dts.com.vn.request.AddServicePackageRequest;
import dts.com.vn.request.IsdnDetailCenterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class IsdnDetailCenterService {
    private final IsdnDetailCenterRepository isdnDetailCenterRepository;

    public IsdnDetailCenterService(IsdnDetailCenterRepository isdnDetailCenterRepository) {
        this.isdnDetailCenterRepository = isdnDetailCenterRepository;
    }

    public Page<IsdnDetailCenter> findAll(String search, Pageable pageable) {
        if (StringUtils.hasLength(search)) {
            return isdnDetailCenterRepository.findAll(search, pageable);
        }
        return isdnDetailCenterRepository.findAll(pageable);
    }

    public IsdnDetailCenter add(IsdnDetailCenterRequest request) {
        IsdnDetailCenter isdnDetailCenter = new IsdnDetailCenter(request);
        return isdnDetailCenterRepository.save(isdnDetailCenter);
    }

    public IsdnDetailCenter update(IsdnDetailCenterRequest request) {

        IsdnDetailCenter isdnDetailCenter = isdnDetailCenterRepository.findById(request.getIsdnDetailCenterId()).orElse(null);
        if (isdnDetailCenter == null) {
            throw new RestApiException(ErrorCode.NOT_FOUND);
        }
        isdnDetailCenter.setCenterId(request.getCenterId());
        isdnDetailCenter.setIsdnPrefix(request.getIsdnPrefix());
        isdnDetailCenter.setNetwork(request.getNetwork());

        return isdnDetailCenterRepository.save(isdnDetailCenter);
    }

    public void delete(Long id) {
        isdnDetailCenterRepository.deleteById(id);
    }
}
