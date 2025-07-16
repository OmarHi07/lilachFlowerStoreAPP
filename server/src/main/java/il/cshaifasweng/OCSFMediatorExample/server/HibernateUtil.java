package il.cshaifasweng.OCSFMediatorExample.server;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.properties");  // נטען את הקובץ שאתה עובד איתו
            configuration.addAnnotatedClass(il.cshaifasweng.OCSFMediatorExample.entities.Complain.class);
            configuration.addAnnotatedClass(il.cshaifasweng.OCSFMediatorExample.entities.Customer.class);
            configuration.addAnnotatedClass(il.cshaifasweng.OCSFMediatorExample.entities.Branch.class);
            configuration.addAnnotatedClass(il.cshaifasweng.OCSFMediatorExample.entities.Order.class);
            // הוסף ישויות נוספות לפי הצורך

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
