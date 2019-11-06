package com.tmx.dao;

import com.tmx.domain.Film;
import com.tmx.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FilmDao {
    public void addFilm(Film film){
        Session session = HibernateUtil.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.save(film);
        transaction.commit();
    }
}
