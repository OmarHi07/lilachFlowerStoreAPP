package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

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

    public List<Flower> getAllFlowers() throws Exception {
        try {
            session.beginTransaction();

            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Flower> criteria = builder.createQuery(Flower.class);
            criteria.from(Flower.class);

            List<Flower> result = session.createQuery(criteria).getResultList();

            session.getTransaction().commit();
            return result;
        } catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            throw exception; // propagate the exception or return an empty list
        }
    }


    public Flower getFlower(int id) {
        Flower flower = null;
        try {
            session.beginTransaction();

            flower = session.get(Flower.class, id);  // 🚀 simple and direct

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }
        return flower;
    }

    public void changePrice(int id, int price) {
        try {
            session.beginTransaction();

            // Fetch the flower by ID
            Flower flower = session.get(Flower.class, id);

            if (flower != null) {
                flower.setPrice(price); // Set new price
                session.update(flower); // Optional: Hibernate tracks changes automatically
            } else {
                System.out.println("Flower with ID " + id + " not found.");
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }


}
