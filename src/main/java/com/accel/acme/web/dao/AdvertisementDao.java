package com.accel.acme.web.dao;

import com.accel.acme.web.model.Advertisement;
import com.accel.acme.web.model.NewsPapersAdvertisements;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdvertisementDao {
    @Autowired
    SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean addAdvertisement(Advertisement advertisement) {
        try {
            sessionFactory.getCurrentSession().save(advertisement);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isRefIdExists(String refId) {

        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Advertisement.class);
        criteria.add(Restrictions.eq("referenceId", refId));
        List<Advertisement> advertisement = criteria.list();

        return !advertisement.isEmpty();

    }

    public boolean updateAdvertisement(Advertisement advertisement) {
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(advertisement);
            sessionFactory.getCurrentSession().flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public Advertisement getAdvertisementById(Integer advertisementId) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Advertisement.class);
        criteria.add(Restrictions.eq("id", advertisementId));
        Advertisement advertisement = (Advertisement) criteria.uniqueResult();
        return advertisement;
    }

    public Advertisement getAdvertisementByCode(String advertisementCode) {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Advertisement.class);
        criteria.add(Restrictions.eq("referenceId", advertisementCode));
        Advertisement advertisement = (Advertisement) criteria.uniqueResult();
        return advertisement;
    }

    public boolean removeAdvertisement(Advertisement advertisement) {
        try {
            sessionFactory.getCurrentSession().delete(advertisement);
            sessionFactory.getCurrentSession().flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Advertisement> listAdvertisements() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Advertisement.class);
        List<Advertisement> advertisementList = (List<Advertisement>) criteria.list();
        return advertisementList;
    }

    public List<NewsPapersAdvertisements> getAllPublishedAdvertisement() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(NewsPapersAdvertisements.class);
        List<NewsPapersAdvertisements> advertisementList = criteria.list();
        return advertisementList;
    }

}
