package dts.com.vn.ilink.service.impl;

import dts.com.vn.entities.RequestSummary;
import dts.com.vn.enumeration.LogConstants;
import dts.com.vn.enumeration.Source;
import dts.com.vn.ilarc.entities.ArchiveSasReRequestMessage;
import dts.com.vn.ilarc.entities.IlArcTaskParameter;
import dts.com.vn.ilarc.repository.ArchiveRequestRepository;
import dts.com.vn.ilarc.repository.IlArcTaskParameterRepository;
import dts.com.vn.ilink.controller.CleanILinkDataController;
import dts.com.vn.ilink.entities.SasReRequestMessage;
import dts.com.vn.ilink.repository.SasReRequestMessageRepository;
import dts.com.vn.ilink.repository.SasReTaskParameterRepository;
import dts.com.vn.ilink.service.CleanDataService;
import dts.com.vn.repository.RequestSummaryRepository;
import dts.com.vn.response.ilink.ILRequestParam;
import dts.com.vn.response.ilink.ILRequestParameter;
import dts.com.vn.util.LogUtil;
import dts.com.vn.util.WebUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class CleanDataServiceImpl implements CleanDataService {

	private static final Logger logger = LoggerFactory.getLogger(CleanILinkDataController.class);
	private final SasReRequestMessageRepository ilinkRequestRepository;
	private final SasReTaskParameterRepository ilinkTaskRepository;
	private final RequestSummaryRepository requestSummaryRepository;
	private final ArchiveRequestRepository archiveRequestRepository;
	private final IlArcTaskParameterRepository archiveTaskParameterRepository;

	@Autowired
	public CleanDataServiceImpl(SasReRequestMessageRepository ilinkRequestRepository,
	                            SasReTaskParameterRepository ilinkTaskRepository,
	                            RequestSummaryRepository requestSummaryRepository,
	                            ArchiveRequestRepository archiveRequestRepository,
	                            IlArcTaskParameterRepository archiveTaskParameterRepository) {
		this.ilinkRequestRepository = ilinkRequestRepository;
		this.ilinkTaskRepository = ilinkTaskRepository;
		this.requestSummaryRepository = requestSummaryRepository;
		this.archiveRequestRepository = archiveRequestRepository;
		this.archiveTaskParameterRepository = archiveTaskParameterRepository;
	}

	@Override
	public void cleanIlinkData(String strStartDate, String strEndDate) {
		// L???y data t??? sas_re_request_message v?? sas_re_request_parameter
		Timestamp startDate = Timestamp.valueOf(strStartDate);
		Timestamp endDate = Timestamp.valueOf(strEndDate);
		List<SasReRequestMessage> listIlinkRequest = ilinkRequestRepository.findAllBeetweenDate(startDate, endDate);
		logger.info("==========> Total request need clean: {}", listIlinkRequest.size());
		if (listIlinkRequest.size() > 0) {
			for (SasReRequestMessage request : listIlinkRequest) {
				List<ILRequestParameter> listInterface = ilinkTaskRepository.findAllByRequestId(request.getRequestId());
				List<ILRequestParam> listParamOfRequest = new ArrayList<>();
				for (ILRequestParameter item : listInterface) {
					ILRequestParam param = new ILRequestParam();
					param.setRequestId(item.getrequestid());
					param.setTaskId(item.gettaskid());
					param.setParamType(item.getparamtype());
					param.setParamName(item.getparamname());
					param.setParamValue(item.getparamvalue());
					listParamOfRequest.add(param);
				}
				logger.info("==========> Total param of request: {}", listParamOfRequest.size());
				// T???o entity ch???a th??ng tin request
				RequestSummary requestSummary = new RequestSummary();
				requestSummary.setRequestId(request.getRequestId());
				requestSummary.setIsdn(WebUtil.formatIsdn(request.getPrimarySubsId()));
				requestSummary.setReceivedDate(request.getReceivedTime());
				requestSummary.setResponseDate(request.getFinishedTime());
				requestSummary.setStatus(request.getStatus());
				// N???u ch??a v??o lu???ng n??o th?? transaction l?? Unknown
				if (request.getMessage() != null &&
						request.getMessage().equalsIgnoreCase("The getInfoAlias returns empty response.")) {
					requestSummary.setTransaction("Unknown");
				}
				// For c??c param ????? l???y c??c th??ng tin c???n thi???t c???a request
				for (ILRequestParam param : listParamOfRequest) {
					// L???c y??u c???u c???a ng?????i d??ng trong list param
					if (param.getParamName().equalsIgnoreCase("SMS_CONTENT")) {
						requestSummary.setRequestCommand(param.getParamValue());
					}
					// L???c message l???i trong list param
					if (param.getParamName().equalsIgnoreCase("SO1_SMESSAGE")) {
						requestSummary.setResponseError(param.getParamValue());
					}
					// L???c message tr??? v??? trong list param
					if (param.getParamName().equalsIgnoreCase("MESSAGE")) {
						requestSummary.setResponseMessage(param.getParamValue());
					}
					// L???c ngu???n g???i y??u c???u trong list param
					if (param.getParamName().equalsIgnoreCase("SOURCE_TYPE")) {
						String chanel = Source.getValue(param.getParamValue());
						requestSummary.setChanel(chanel);
					}
					// N???u v??o lu???ng th?? l???c lu???ng trong list param
					if (param.getParamName().equalsIgnoreCase("TRANS_CODE")) {
						requestSummary.setTransaction(param.getParamValue());
					}
				}
				LogUtil.writeLog(logger, LogConstants.REQUEST, requestSummary);
				requestSummaryRepository.saveAndFlush(requestSummary);
			}
		}
	}

	@Override
	public void cleanArchiveData(Integer startRequestId, Integer endRequestId) {
		logger.info("==========> Start requestId: {}, End requestId: {}", startRequestId, endRequestId);
		for (long i = startRequestId; i < endRequestId; i++) {
			// T??m request tr??n db ilarc(archive)
			Optional<ArchiveSasReRequestMessage> optRequest = archiveRequestRepository.findById(i);
			if (optRequest.isPresent()) {
				ArchiveSasReRequestMessage request = optRequest.get();
				// T??m nh???ng th??ng tin c???n c???a request
				List<IlArcTaskParameter> lstParam = archiveTaskParameterRepository.findParamsNeeded(request.getRequestId());
				// Remove nh??ng param tr??ng paramName
				List<IlArcTaskParameter> lstParamUnique = lstParam.stream()
				                                                  .collect(collectingAndThen(toCollection(
						                                                  () -> new TreeSet<>(Comparator.comparing(IlArcTaskParameter::getParameterName))), ArrayList::new));
				List<ILRequestParam> listParamOfRequest = new ArrayList<>();
				for (IlArcTaskParameter item : lstParamUnique) {
					ILRequestParam param = new ILRequestParam();
					param.setRequestId(request.getRequestId());
					param.setTaskId(item.getEmbeddableWithoutId().getTaskId().intValue());
					param.setParamType(item.getEmbeddableWithoutId().getParametersType().intValue());
					param.setParamName(item.getParameterName());
					param.setParamValue(item.getEmbeddableWithoutId().getParametersValue());
					listParamOfRequest.add(param);
				}
				// T???o entity ch???a th??ng tin request
				RequestSummary requestSummary = new RequestSummary();
				requestSummary.setRequestId(request.getRequestId());
				requestSummary.setIsdn(WebUtil.formatIsdn(request.getPrimarySubsId()));
				requestSummary.setReceivedDate(request.getReceivedTime());
				requestSummary.setResponseDate(request.getFinishedTime());
				requestSummary.setStatus(request.getStatus());
				// N???u ch??a v??o lu???ng n??o th?? transaction l?? Unknown
				if (request.getMessage() != null &&
						request.getMessage().equalsIgnoreCase("The getInfoAlias returns empty response.")) {
					requestSummary.setTransaction("Unknown");
				}
				// For c??c param ????? l???y c??c th??ng tin c???n thi???t c???a request
				for (ILRequestParam param : listParamOfRequest) {
					// L???c y??u c???u c???a ng?????i d??ng trong list param
					if (param.getParamName().equalsIgnoreCase("SMS_CONTENT")) {
						requestSummary.setRequestCommand(param.getParamValue());
					}
					// L???c message l???i trong list param
					if (param.getParamName().equalsIgnoreCase("SO1_SMESSAGE")) {
						requestSummary.setResponseError(param.getParamValue());
					}
					// L???c message tr??? v??? trong list param
					if (param.getParamName().equalsIgnoreCase("MESSAGE")) {
						requestSummary.setResponseMessage(param.getParamValue());
					}
					// L???c ngu???n g???i y??u c???u trong list param
					if (param.getParamName().equalsIgnoreCase("SOURCE_TYPE")) {
						String chanel = Source.getValue(param.getParamValue());
						requestSummary.setChanel(chanel);
					}
					// N???u v??o lu???ng th?? l???c lu???ng trong list param
					if (param.getParamName().equalsIgnoreCase("TRANS_CODE")) {
						requestSummary.setTransaction(param.getParamValue());
					}
				}
				LogUtil.writeLog(logger, LogConstants.REQUEST, requestSummary);
				requestSummaryRepository.saveAndFlush(requestSummary);
			}
		}
	}

	@Override
	public void getShortcode(Integer startRequestId, Integer endRequestId) {
		logger.info("==========> Start requestId: {}, End requestId: {}", startRequestId, endRequestId);
		for (long i = startRequestId; i < endRequestId; i++) {
			// T??m request tr??n db ilarc(archive)
			Optional<RequestSummary> optRequestSummary = requestSummaryRepository.findById(i);
			if (optRequestSummary.isPresent()) {
				RequestSummary requestSummary = optRequestSummary.get();
				logger.info("==========> RequestId: {}", requestSummary.getRequestId());
				// T??m th??ng tin shorcode c???a request tr??n db ilarc
				Optional<IlArcTaskParameter> optShortCode =
						archiveTaskParameterRepository.findShortCode(requestSummary.getRequestId());
				if (optShortCode.isPresent()) {
					IlArcTaskParameter shorcode = optShortCode.get();
					requestSummary.setServiceNumber(Integer.parseInt(shorcode.getEmbeddableWithoutId()
					                                                         .getParametersValue()));
					LogUtil.writeLog(logger, LogConstants.REQUEST, requestSummary);
					requestSummaryRepository.saveAndFlush(requestSummary);
				}
			}
		}
	}

}
