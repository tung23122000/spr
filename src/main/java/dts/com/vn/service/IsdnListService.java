package dts.com.vn.service;

import dts.com.vn.entities.IsdnList;
import dts.com.vn.repository.IsdnListRepository;
import org.springframework.stereotype.Service;

@Service
public class IsdnListService {
    private final IsdnListRepository isdnListRepository;

    public IsdnListService(IsdnListRepository isdnListRepository) {
        this.isdnListRepository = isdnListRepository;
    }

    public IsdnList save(IsdnList isdnList){
        return isdnListRepository.save(isdnList);
    }
}
