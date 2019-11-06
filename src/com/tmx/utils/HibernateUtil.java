package com.tmx.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    public static final SessionFactory sessionFactory;
    static {
        //1.加载配置文件
        Configuration configure = new Configuration().configure();
        //configure.addResource("com/myxq/domain/Customer.hbm.xml");
        //2.创建sessionFactory   --JDBC 连接池
        sessionFactory = configure.buildSessionFactory();
    }
    public static Session openSession(){
        Session session = sessionFactory.openSession();
        return  session;
    }
    public static Session getCurrentSession(){
        Session session = sessionFactory.getCurrentSession();
        return  session;
    }

}
