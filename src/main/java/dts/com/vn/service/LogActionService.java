package dts.com.vn.service;

import dts.com.vn.entities.LogAction;
import dts.com.vn.repository.LogActionRepository;
import dts.com.vn.request.LogActionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class LogActionService {

    private final LogActionRepository logActionRepository;

    public LogActionService(LogActionRepository logActionRepository) {
        this.logActionRepository = logActionRepository;
    }

    public Page<LogAction> findAll(LogActionRequest logActionRequest, Pageable pageable) {
        Timestamp startDate = null;
        Timestamp endDate = null;
        if (logActionRequest.getStartDate() != null){
            startDate = Timestamp.valueOf(logActionRequest.getStartDate() + " " + "00:00:00");
        }
        if (logActionRequest.getEndDate() != null){
            endDate = Timestamp.valueOf(logActionRequest.getEndDate() + " " + "23:59:59");
        }

        return logActionRepository.findAll(logActionRequest.getAction(), startDate, endDate, pageable);
    }

    public LogAction add(LogAction logAction){
        return logActionRepository.save(logAction);
    }
}
