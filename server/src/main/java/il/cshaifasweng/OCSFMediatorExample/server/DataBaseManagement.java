package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class DataBaseManagement {
    public DataBaseManagement() {
        SessionFactory sessionFactory = getSessionFactory();
        session = sessionFactory.openSession();
    }
    private static Session session;

    private static SessionFactory getSessionFactory() throws HibernateException {
        Configuration configuration = new Configuration();

        System.out.println("Enter dataBase password: ");
        Scanner scanner = new Scanner(System.in);
        String password = scanner.nextLine();
        scanner.close();

        configuration.setProperty("hibernate.connection.password", password);

        // Add ALL of your entities here. You can also try adding a whole package.
        configuration.addAnnotatedClass(Customer.class);
        configuration.addAnnotatedClass(Branch.class);
        configuration.addAnnotatedClass(BranchManager.class);
        configuration.addAnnotatedClass(Employee.class);
        configuration.addAnnotatedClass(Complain.class);
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(CartProduct.class);
        configuration.addAnnotatedClass(NetworkWorker.class);
        configuration.addAnnotatedClass(StoreChainManager.class);
        configuration.addAnnotatedClass(SystemAdmin.class);
        configuration.addAnnotatedClass(CostumerServiceEmployee.class);
        configuration.addAnnotatedClass(Flower.class);

        ServiceRegistry serviceRegistry = new
                StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();

        return configuration.buildSessionFactory(serviceRegistry);
    }

    private static void generateFlowers() throws Exception {

        Long count = (Long) session.createQuery("select count(f) from Flower f").uniqueResult();
        if (count == 0 ) {
            InputStream is = DataBaseManagement.class.getResourceAsStream("/images/0.png");
            byte[] imageBytes = is.readAllBytes();
            Flower flower1 = new Flower("Whisper of love", "Dozens of deep red roses tightly wrapped — bold, luxurious, and intense", 250, "Dozens of red roses", imageBytes);
            session.save(flower1);
            is = DataBaseManagement.class.getResourceAsStream("/images/1.png");
            imageBytes = is.readAllBytes();
            Flower flower2 = new Flower("SunShine Meadow", "A rustic bouquet full of sunflowers,white blooms,and tiny blue and yellow flowers ", 160, "bouquet full of sunflowers", imageBytes);
            session.save(flower2);
            is = DataBaseManagement.class.getResourceAsStream("/images/2.png");
            imageBytes = is.readAllBytes();
            Flower flower3 = new Flower("Tropical Sunrise", " A vibrant and colorful mix with orange, pink, and yellow tones", 150, "A colorful mix", imageBytes);
            session.save(flower3);
            is = DataBaseManagement.class.getResourceAsStream("/images/3.png");
            imageBytes = is.readAllBytes();
            Flower flower4 = new Flower("Velvet touch", "A sleek and elegant single red rose in premium wrapping", 20, "A single red rose", imageBytes);
            session.save(flower4);
            is = DataBaseManagement.class.getResourceAsStream("/images/4.png");
            imageBytes = is.readAllBytes();
            Flower flower5 = new Flower("Eternal Grace", " Classic combination of red and white symbolizing love and purity", 200, "Classic combination", imageBytes);
            session.save(flower5);
            /*
             * The call to session.flush() updates the DB immediately without ending the transaction.
             * Recommended to do after an arbitrary unit of work.
             * MANDATORY to do if you are saving a large amount of data - otherwise you may get cache errors.
             */
            session.flush();
        }
    }

    private static void generateBranches() throws Exception {
        Long count = (Long) session.createQuery("select count(b) from Branch b").uniqueResult();
        if (count == 0) {
            Branch branch1 = new Branch("Makr");
            Branch branch2 = new Branch("Nazareth");
            Branch branch3 = new Branch("Kafr Manda");
            Branch branch4 = new Branch("Rame");
            Branch branch5 = new Branch("Sakhnin");

            session.save(branch1);
            session.save(branch2);
            session.save(branch3);
            session.save(branch4);
            session.save(branch5);

            session.flush(); // Commit to DB before customer registration
        }
    }


    public void initDataBase(){
        try{
            session.beginTransaction();

            generateFlowers();
            generateBranches();

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

            flower = session.get(Flower.class, id);

            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }
        return flower;
    }

    public void changePriceDB(int id, double price) {
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

    public static boolean isUsernameTaken(String username) {
        try {
            session.beginTransaction();

            Long count = (Long) session.createQuery(
                            "SELECT COUNT(c) FROM Customer c WHERE c.username = :username")
                    .setParameter("username", username)
                    .uniqueResult();

            session.getTransaction().commit();
            return count != null && count > 0;
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return true; // Fail-safe: assume taken on error
        }
    }

    public static SignUpResponse registerCustomer(SignUpRequest req) {
        try {
            session.beginTransaction();

            // Optional: check again if username/email/phone already exist to be extra safe
            Long count = (Long) session.createQuery(
                            "SELECT COUNT(c) FROM Customer c WHERE c.username = :username OR c.email = :email OR c.phone = :phone OR c.identifyingNumber = :id")
                    .setParameter("username", req.getUsername())
                    .setParameter("email", req.getEmail())
                    .setParameter("phone", req.getPhone())
                    .setParameter("id", req.getId())
                    .uniqueResult();

            if (count != null && count > 0) {
                session.getTransaction().rollback();
                return new SignUpResponse(false, "Account with these credentials already exists.");
            }

            // Map account type to customerType
            int type = switch (req.getAccountType()) {
                case "Membership" -> 3;
                case "Network Account" -> 2;
                default -> 1; // Regular
            };

            Customer customer = new Customer(
                    req.getFirstName(), req.getLastName(), req.getEmail(), req.getPhone(),
                    req.getUsername(), req.getPassword(), req.getCreditCard(), req.getExpiryDate(),
                    req.getCvv(), req.getId(), type
            );

            // Branch assignment logic
            if (type == 1) {  // Regular: assign selected branch
                Branch selectedBranch = (Branch) session
                        .createQuery("FROM Branch WHERE address = :address")
                        .setParameter("address", req.getBranch())
                        .uniqueResult();

                if (selectedBranch != null) {
                    customer.addStore(selectedBranch);  // will handle both sides
                } else {
                    session.getTransaction().rollback();
                    return new SignUpResponse(false, "Selected branch not found.");
                }

            } else {  // Network or Membership: assign all branches
                List<Branch> allBranches = session
                        .createQuery("FROM Branch", Branch.class)
                        .getResultList();

                customer.addStore2(allBranches);  // adds both ways
            }

            session.save(customer);
            session.getTransaction().commit();

            return new SignUpResponse(true, "Account created successfully.");
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return new SignUpResponse(false, "An error occurred while creating the account.");
        }
    }






}
