package dts.com.vn.repository;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

@Repository
public class Report5Repository {

    //Lấy tổng số thuê bao huỷ gói từ đầu tháng đến thời điểm tra cứu
    public Long getTotalPackageDelete(EntityManager entityManager, String partition) {
        String queryStr = "SELECT COUNT ( reg_id ) " +
                " FROM " + partition +
                " WHERE end_datetime BETWEEN date_trunc( 'month', CURRENT_DATE ) AND NOW() ";
        try {
            Query query = entityManager.createNativeQuery(queryStr);
            long count = 0;
            List<BigInteger> list = query.getResultList();
            for (BigInteger bigInteger : list) {
                if(bigInteger!=null){
                    count = bigInteger.longValue();
                }
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Lấy tổng số thuê bao đăng ký hoặc gia hạn từ đầu tháng đến thời điểm tra cứu
    public Long getTotalPackageActiveOrRenew(EntityManager entityManager, String partition) {
        String queryStr = "SELECT COUNT ( reg_id ) " +
                " FROM " + partition +
                " WHERE sta_datetime BETWEEN date_trunc( 'month', CURRENT_DATE ) AND NOW() ";
        try {
            Query query = entityManager.createNativeQuery(queryStr);
            long count = 0;
            List<BigInteger> list = query.getResultList();
            for (BigInteger bigInteger : list) {
                if(bigInteger!=null){
                    count = bigInteger.longValue();
                }
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    //Lấy tổng số thuê bao đăng ký hoặc gia hạn từ đầu tháng đến thời điểm tra cứu
    public Long getTotalChargePrice(EntityManager entityManager, String partition, String startTime,
                                    String endTime) {
        String queryStr =" SELECT SUM(TO_NUMBER(charge_price, '9G999g999')) " +
                " FROM " + partition +
                " WHERE sta_datetime BETWEEN '"+ startTime +"'  AND  '"+ endTime +"'";
        try {
            Query query = entityManager.createNativeQuery(queryStr);
            long count = 0;
            List<BigDecimal> list = query.getResultList();
            if (list.get(0)!=null){
                count = list.get(0).longValue();
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
