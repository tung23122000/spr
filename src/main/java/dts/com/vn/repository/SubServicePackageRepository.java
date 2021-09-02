package dts.com.vn.repository;

import dts.com.vn.entities.SubServicePackage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


@Repository
public class SubServicePackageRepository {

    private final EntityManager entityManager;

    public SubServicePackageRepository(@Qualifier("entityManagerFactory") EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Object[]> findGroupByPackageId(Long packageId) {
        Query query = this.entityManager.createNativeQuery("SELECT package_id as packageId, sub_package_id as subPackageId FROM sub_service_package ssp " +
                " WHERE (ssp.package_id = " + packageId + " OR ssp.sub_package_id = " + packageId + ") " +
                " GROUP BY ssp.package_id, ssp.sub_package_id");
        List<Object[]> list = query.getResultList();
        return list;
    }

//    public List<Object[]> checkExist(SubServicePackage subServicePackage) {
//        List<Object[]> object = this.entityManager.createNativeQuery("SELECT * FROM sub_service_package ssp " +
//                " WHERE (ssp.package_id = " + subServicePackage.getPackageId() + " AND ssp.sub_package_id = " + subServicePackage.getSubPackageId() + ") " +
//                " OR (ssp.package_id = " + subServicePackage.getPackageId() + " AND ssp.sub_package_id = " + subServicePackage.getSubPackageId() + ")").getResultList();
//        return object;
//    }
//
    public void save(SubServicePackage subServicePackage) {
        this.entityManager.createNativeQuery("INSERT INTO sub_service_package " +
                " values(" + subServicePackage.getPackageId() + "," + subServicePackage.getSubPackageId() + ")").executeUpdate();
    }
//
    public void delete(SubServicePackage subServicePackage) {
        this.entityManager.createNativeQuery("DELETE FROM sub_service_package ssp " +
                " WHERE ssp.package_id = " + subServicePackage.getPackageId() +
                " AND ssp.sub_package_id = " + subServicePackage.getSubPackageId()).executeUpdate();

    }
//
//
//    public void deActive(SubServicePackage subServicePackage) {
//        this.entityManager.createNativeQuery("DELETE FROM sub_service_package " +
//                " values(" + subServicePackage.getPackageId() + "," + subServicePackage.getSubPackageId() + ",'1')").executeUpdate();
//    }

}
