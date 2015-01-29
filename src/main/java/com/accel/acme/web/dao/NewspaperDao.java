package com.accel.acme.web.dao;

import com.accel.acme.web.model.Newspaper;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("newspaperDao")
public class NewspaperDao {

    @Autowired
    SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public boolean addNewspaper(Newspaper newspaper) {
        sessionFactory.getCurrentSession().save(newspaper);
        return true;
    }

    public boolean updateNewspaper(Newspaper newspaper) {
        sessionFactory.getCurrentSession().saveOrUpdate(newspaper);
        return true;
    }

    public Newspaper getNewspaperById(Integer newspaperId) {
        Criteria newspaperCriteria = sessionFactory.getCurrentSession().createCriteria(Newspaper.class);
        newspaperCriteria.add(Restrictions.eq("id", newspaperId));
        return (Newspaper) newspaperCriteria.uniqueResult();
    }

    public Newspaper getNewspaperByName(String newspaperName) {
        Criteria newspaperCriteria = sessionFactory.getCurrentSession().createCriteria(Newspaper.class);
        newspaperCriteria.add(Restrictions.eq("name", newspaperName));
        return (Newspaper) newspaperCriteria.uniqueResult();
    }

    public Newspaper getNewspaperByCode(String newspaperCode) {
        Criteria newspaperCriteria = sessionFactory.getCurrentSession().createCriteria(Newspaper.class);
        newspaperCriteria.add(Restrictions.eq("code", newspaperCode));
        return (Newspaper) newspaperCriteria.uniqueResult();
    }

    public boolean removeNewspaper(Newspaper newspaper) {
        sessionFactory.getCurrentSession().delete(newspaper);
        return true;
    }

    @SuppressWarnings("unchecked")
    public List<Newspaper> listNewspapers() {
        return sessionFactory.getCurrentSession().createCriteria(Newspaper.class).list();
    }

}
