package dts.com.vn.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Repository
public class Report4Repository {

    public Long getTotalExtRtryThree(EntityManager entityManager, String partition, String packageId) {
        String queryStr = "SELECT COUNT ( reg_id )  FROM "+ partition +
                " WHERE ext_retry_num = 3 " +
                " AND end_datetime ISNULL " +
                " AND package_id = " + packageId +
                " AND expire_datetime < ( NOW( ) + INTERVAL '30 MINUTE' )";
        try {
            Query query = entityManager.createNativeQuery(queryStr);
            long count = 0;
            List<BigInteger> list = query.getResultList();
            for (BigInteger bigInteger : list) {
                    count = bigInteger.longValue();
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public Long getTotalNotRetryYet(EntityManager entityManager, String partition, String packageId) {
        String queryStr = "SELECT COUNT ( reg_id ) FROM "+ partition +
                "WHERE ext_retry_num != 3 " +
                " AND package_id = " + packageId +
                " AND expire_datetime < ( NOW( ) - INTERVAL '30 MINUTE' )";
        try {
            Query query = entityManager.createNativeQuery(queryStr);
            long count = 0;
            List<BigInteger> list = query.getResultList();
            for (BigInteger bigInteger : list) {
                    count = bigInteger.longValue();
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
