package dts.com.vn.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Repository
public class ListPackageResponseRepository {

    public Long getTotalNumberOfSubscribers(EntityManager entityManager, String partition, String packageCode,
                                            String date) {
        String date1 = "'" + date + " 23:59:59'";
        String queryStr = "SELECT COUNT(DISTINCT isdn)\n" +
                "FROM" + partition +
                "WHERE package_id =" + packageCode +
                "AND ((" + date1 + " BETWEEN sta_datetime AND end_datetime) \n" +
                "OR (end_datetime IS NULL AND " + date1 + " > sta_datetime ))";
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
