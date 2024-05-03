package com.hairsalon.respository.imp;

import com.hairsalon.respository.IRevenueRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.Query;

@Repository
@Transactional
public class RevenueRepositoryImp implements IRevenueRepository {

    @Autowired
    SessionFactory sessionFactory;


    @Override
    public Double getRevenueFromService() {
        Double res = 0.0;
        StringBuilder hql = new StringBuilder("Select SUM(SH.price) from Appointment A INNER" +
                " JOIN ServiceHair SH ON A.serviceHair.id = SH.id AND A.appointmentStatus.id = 4");
        try {
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql.toString());
            res = (Double) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res == null) {
            return 0.0;
        }
        return res;
    }

    @Override
    public Double getRevenueFromProduct() {
        Double res = 0.0;
        StringBuilder hql = new StringBuilder("Select SUM(OT.price) from Order O INNER" +
                " JOIN OrderItem OT ON O.id = OT.order.id AND O.orderStatus.id = 5");
        try {
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql.toString());
            res = (Double) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res == null) {
            return 0.0;
        }
        return res;
    }

    @Override
    public Double getRevenueFromServiceByYear(Integer year) {
        Double res = 0.0;
        StringBuilder hql = new StringBuilder("Select SUM(SH.price) from Appointment A INNER" +
                " JOIN ServiceHair SH ON A.serviceHair.id = SH.id AND A.appointmentStatus.id = 4 and YEAR(A.appointmentDate) = :year");
        try {
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql.toString());
            query.setParameter("year", year);
            res = (Double) query.getSingleResult();
        } catch (Exception e) {

        }
        if (res == null) {
            return 0.0;
        }
        return res;
    }

    @Override
    public Double getRevenueFromServiceByMonth(Integer year, Integer month) {
        Double res = 0.0;
        StringBuilder hql = new StringBuilder("Select SUM(SH.price) from Appointment A INNER" +
                " JOIN ServiceHair SH ON A.serviceHair.id = SH.id AND A.appointmentStatus.id = 4 and MONTH(A.appointmentDate) = :month " +
                "AND YEAR(A.appointmentDate) = :year");
        try {
            Session session = sessionFactory.openSession();
            Query query = session.createQuery(hql.toString());
            query.setParameter("month", month);
            query.setParameter("year", year);
            res = (Double) query.getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res == null) {
            return 0.0;
        }
        return res;
    }
}
