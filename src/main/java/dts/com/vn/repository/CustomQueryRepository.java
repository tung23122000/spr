package dts.com.vn.repository;

import dts.com.vn.entities.RenewData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomQueryRepository {
    private static final Logger logger = LoggerFactory.getLogger(CustomQueryRepository.class);

    private final EntityManager entityManager;

    public CustomQueryRepository(@Qualifier("entityManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<RenewData> selectRenewDate(String sql){
        List<Object[]> listObject = entityManager.createNativeQuery(sql).getResultList();
        List<RenewData> returnList = new ArrayList<>();
        for (Object[] objects: listObject) {
            RenewData renewData = new RenewData();
            renewData.setRegId(((BigInteger) objects[0]).longValue());
            renewData.setIsdn((String) objects[1]);
            renewData.setServiceNumber((String) objects[2]);
            renewData.setGroupCode((String) objects[3]);
            renewData.setCommandCode((String) objects[4]);
            renewData.setSourceCode((String) objects[5]);
            System.out.println(objects[6]);
            if (objects[6] == null){
                renewData.setExtRetryNum(null);
            }else{
                renewData.setExtRetryNum(((BigDecimal) objects[6]).longValue());
            }
            returnList.add(renewData);
        }
        return returnList;
    }
}
