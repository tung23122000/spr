package dts.com.vn.service;

import dts.com.vn.entities.IsdnList;
import dts.com.vn.entities.ListDetail;
import dts.com.vn.repository.ListDetailRepository;
import org.springframework.stereotype.Service;

@Service
public class ListDetailService {
    private final ListDetailRepository listDetailRepository;

    public ListDetailService(ListDetailRepository listDetailRepository) {
        this.listDetailRepository = listDetailRepository;
    }

    public ListDetail save(ListDetail listDetail){
        return listDetailRepository.save(listDetail);
    }
}
