package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class DataBaseManagement {
    public DataBaseManagement() {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
    }
    private static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();

        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Flower.class);

        ServiceRegistry serviceRegistry = new
                StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static void generateFlowers() throws Exception {

        Flower flower1 = new Flower("Whisper of love" ,"Dozens of deep red roses tightly wrapped — bold, luxurious, and intense", 250);
        session.save(flower1);
        Flower flower2 = new Flower("SunShine Meadow","A rustic bouquet full of sunflowers,white blooms,and tiny blue and yellow flowers " ,160);
        session.save(flower2);
        Flower flower3 = new Flower("Tropical Sunrise"," A vibrant and colorful mix with orange, pink, and yellow tones",150);
        session.save(flower3);
        Flower flower4 = new Flower("Velvet touch","A sleek and elegant single red rose in premium wrapping",20);
        session.save(flower4);
        Flower flower5 = new Flower("Eternal Grace"," Classic combination of red and white symbolizing love and purity",200);
        session.save(flower5);
        /*
         * The call to session.flush() updates the DB immediately without ending the transaction.
         * Recommended to do after an arbitrary unit of work.
         * MANDATORY to do if you are saving a large amount of data - otherwise you may get cache errors.
         */
        session.flush();

    }

    public void initDataBase(){
        try{
            session.beginTransaction();

            generateFlowers();
            session.getTransaction().commit();
        }
        catch (Exception exception) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        }
    }
    //public Flower getFlower(String id){
       // session.beginTransaction();

    //}
}
