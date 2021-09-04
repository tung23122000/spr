package dts.com.vn.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomQueryRepository {
    private static final Logger logger = LoggerFactory.getLogger(CustomQueryRepository.class);

    private final EntityManager entityManager;

    public CustomQueryRepository(@Qualifier("entityManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public <T> Integer countByFilter(Class<T> classType, String sql) {
        List<Object[]> lst = entityManager.createNativeQuery(sql).getResultList();
        return Integer.valueOf(lst.size());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> selectByFilterAndSortAndPage(Class<T> classType, String sql) {
        List<T> arrObject = this.entityManager.createNativeQuery(sql, classType).getResultList();
        return arrObject;
    }

    public Object countRecord(String sql) {
        try {
            return this.entityManager.createNativeQuery(sql).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("RegisterRepository e: " + e);
            return Integer.valueOf(0);
        }
    }
}
