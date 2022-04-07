package dts.com.vn.service;

import dts.com.vn.entities.CcspInfo;
import dts.com.vn.entities.ServicePackage;
import dts.com.vn.entities.ServiceProgram;
import dts.com.vn.enumeration.Constant;
import dts.com.vn.enumeration.ErrorCode;
import dts.com.vn.exception.RestApiException;
import dts.com.vn.repository.CcspInfoRepository;
import dts.com.vn.repository.ServicePackageRepository;
import dts.com.vn.repository.ServiceProgramRepository;
import dts.com.vn.request.CcspInfoRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CcspInfoService {
    private final CcspInfoRepository ccspInfoRepository;
    private final ServicePackageRepository servicePackageRepository;
    private final ServiceProgramRepository serviceProgramRepository;

    public CcspInfoService(CcspInfoRepository ccspInfoRepository, ServicePackageRepository servicePackageRepository,
                           ServiceProgramRepository serviceProgramRepository) {
        this.ccspInfoRepository = ccspInfoRepository;
        this.servicePackageRepository = servicePackageRepository;
        this.serviceProgramRepository = serviceProgramRepository;
    }

    public CcspInfo saveCcspInfo(CcspInfoRequest ccspInfoRequest){
        CcspInfo ccspInfoReturn;
        ServicePackage servicePackage = servicePackageRepository.findByPackageId(ccspInfoRequest.getServicePackageId());
        ServiceProgram serviceProgram = serviceProgramRepository.findById(ccspInfoRequest.getServiceProgramId()).orElse(null);
        if (servicePackage == null) {
            // SERVICE_PACKAGE_NOT_FOUND
            throw new RestApiException(ErrorCode.SERVICE_PACKAGE_NOT_FOUND);
        } else if (serviceProgram ==  null) {
            // SERVICE_PROGRAM_NOT_FOUND
            throw new RestApiException(ErrorCode.SERVICE_PROGRAM_NOT_FOUND);
        } else {
            // SAVE CCSP_INFO
            ccspInfoReturn = ccspInfoRepository.save(new CcspInfo(ccspInfoRequest, servicePackage, serviceProgram));

            // SAVE CCSP ON SERVICE_PROGRAM TABLE
            if (ccspInfoRequest.getFoName().equals(Constant.FO_SERVICE_CODE)) {
                serviceProgram.setCcspServiceCode(ccspInfoRequest.getCcspValue());
                serviceProgramRepository.save(serviceProgram);
            }
            if (ccspInfoRequest.getFoName().startsWith(Constant.FO_RESULT)) {
                // FIND CCSP INFO WITH PROGRAM_ID AND START WITH FO_RESULT
                List<CcspInfo> ccspInfoList = ccspInfoRepository.findByProgramIdAndFoResult(ccspInfoRequest.getServiceProgramId(), Constant.FO_RESULT);
                String ccspResultCode = "";
                if (ccspInfoList.size() > 0) {
                    for (int i = 0; i < ccspInfoList.size(); i++) {
                        if (i == 0) {
                            ccspResultCode = ccspInfoList.get(i).getCcspValue();
                        } else {
                            ccspResultCode += "#" + ccspInfoList.get(i).getCcspValue();
                        }
                    }
                } else {
                    ccspResultCode = ccspInfoRequest.getCcspValue();
                }
                serviceProgram.setCcspResultCode(ccspResultCode);
                serviceProgramRepository.save(serviceProgram);
            }
        }
        return ccspInfoReturn;
    }
}
