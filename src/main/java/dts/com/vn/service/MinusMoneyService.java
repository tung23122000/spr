package dts.com.vn.service;

import dts.com.vn.entities.MinusMoney;
import dts.com.vn.entities.PrefixInfo;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.MinusMoneyRepository;
import dts.com.vn.repository.ServicePackageRepository;
import dts.com.vn.repository.ServiceProgramRepository;
import dts.com.vn.request.MinusMoneyRequest;
import dts.com.vn.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MinusMoneyService {
    private final MinusMoneyRepository minusMoneyRepository;

    private final ServicePackageRepository servicePackageRepository;

    private final ServiceProgramRepository serviceProgramRepository;

    public MinusMoneyService(MinusMoneyRepository minusMoneyRepository, ServicePackageRepository servicePackageRepository, ServiceProgramRepository serviceProgramRepository) {
        this.minusMoneyRepository = minusMoneyRepository;
        this.servicePackageRepository = servicePackageRepository;
        this.serviceProgramRepository = serviceProgramRepository;
    }

    public MinusMoney save(MinusMoneyRequest minusMoneyRequest){
        ServicePackage servicePackage = servicePackageRepository.findById(minusMoneyRequest.getServicePackage())
                .orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND));
        ServiceProgram serviceProgram = serviceProgramRepository.findById(minusMoneyRequest.getServiceProgram())
                .orElseThrow(() -> new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND));
        MinusMoney minusMoney = new MinusMoney();
        if (minusMoneyRequest.getMinusMoneyLadderId() != null){
            minusMoney.setMinusMoneyLadderId(minusMoneyRequest.getMinusMoneyLadderId());
        }
        minusMoney.setServicePackage(servicePackage);
        minusMoney.setServiceProgram(serviceProgram);
        minusMoney.setMinusAmount(minusMoneyRequest.getMinusAmount());
        minusMoney.setExpiredDay(minusMoneyRequest.getExpiredDay());
        return minusMoneyRepository.save(minusMoney);
    }

    public List<MinusMoney> getAll(Long packageId, Long programId){
        return minusMoneyRepository.getAll(packageId, programId);
    }

    public void delete(Long minusMoneyLadderId){
        minusMoneyRepository.deleteById(minusMoneyLadderId);
    }
}
