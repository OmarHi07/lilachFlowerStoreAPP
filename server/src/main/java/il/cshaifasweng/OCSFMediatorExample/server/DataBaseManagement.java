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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

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
            Flower flower1 = new Flower("Whisper of love", "Dozens of red roses", 250, imageBytes, "Red", 1);
            session.save(flower1);
            is = DataBaseManagement.class.getResourceAsStream("/images/1.png");
            imageBytes = is.readAllBytes();
            Flower flower2 = new Flower("SunShine Meadow", "bouquet full of sunflowers", 160,imageBytes, "Yellow", 1);
            session.save(flower2);
            is = DataBaseManagement.class.getResourceAsStream("/images/2.png");
            imageBytes = is.readAllBytes();
            Flower flower3 = new Flower("Tropical Sunrise", "A colorful mix", 150, imageBytes, "Yellow", 1);
            session.save(flower3);
            is = DataBaseManagement.class.getResourceAsStream("/images/3.png");
            imageBytes = is.readAllBytes();
            Flower flower4 = new Flower("Velvet touch", "A single red rose", 20, imageBytes, "Blue", 1);
            session.save(flower4);
            is = DataBaseManagement.class.getResourceAsStream("/images/4.png");
            imageBytes = is.readAllBytes();
            Flower flower5 = new Flower("Eternal Grace", "Classic combination", 200, imageBytes, "White", 2);
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
            Branch branch1 = new Branch("Haifa");
            Branch branch2 = new Branch("TelAviv");

            session.save(branch1);
            session.save(branch2);

            session.flush(); // Commit to DB before customer registration
        }
    }

    private static void generateUsers() throws Exception {
        Long count = (Long) session.createQuery("select count(b) from Customer b").uniqueResult();
        if (count == 0) {
            Customer Cu1 = new Customer("Arkan", "Issa", "Arkanissa7@gmail.com", "0500118796", "Arkan7", "123456", "1457125896543267", "0727", "148", "872136", 2);
            NetworkWorker nw = new NetworkWorker("ACS", "AERT", "ghty");
            session.save(nw);
            session.save(Cu1);
            session.flush(); // Commit to DB before customer registration
        }
    }


    public Flower addFlower(Flower flower) {
        try {
            session.beginTransaction();
            session.save(flower);
            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            exception.printStackTrace();
        }
        return flower;
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
        }
        catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            throw exception; // propagate the exception or return an empty list
        }
    }

    public List<Order> getUser(int id) {
        Customer user = null;
        List<Order> orders = null;
        try {
            session.beginTransaction();
            user = session.get(Customer.class, id);
            orders= user.getListOrders();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }
        return orders;

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


    //Added by arkan
    public void deleteFlower(int id){
        try{
            session.beginTransaction();
            Flower flower = session.get(Flower.class, id);
            if (flower != null) {
                session.delete(flower);
                session.getTransaction().commit();
            }
            else {
                System.out.println("Flower with ID " + id + " not found.");
            }
        }
        catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            exception.printStackTrace();
        }
    }


    //Added by arkan
    public List<Branch> getAllBranches() throws Exception {
        try {
            session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Branch> criteria = builder.createQuery(Branch.class);
            criteria.from(Branch.class);

            List<Branch> result = session.createQuery(criteria).getResultList();

            session.getTransaction().commit();
            return result;
        }
        catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("An error occurred, changes have been rolled back.");
            exception.printStackTrace();
            throw exception; // propagate the exception or return an empty list
        }
    }
    public List<Flower> getAllFlowersNoTx() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Flower> criteria = builder.createQuery(Flower.class);
        criteria.from(Flower.class);
        return session.createQuery(criteria).getResultList();
    }

    public List<Branch> getAllBranchesNoTx() throws Exception {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Branch> criteria = builder.createQuery(Branch.class);
        criteria.from(Branch.class);
        return session.createQuery(criteria).getResultList();
    }


    public void initDataBase(){
        try{
            session.beginTransaction();

            generateFlowers();
            generateBranches();
            generateUsers();
            List<Flower> flowers = getAllFlowersNoTx();
            List<Branch> branches = getAllBranchesNoTx();
            for (Flower flower : flowers) {
                flower.setBranch(new ArrayList<>(branches));
                session.update(flower);
            }

            session.getTransaction().commit();
        }
        catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.err.println("An error occured, changes have been rolled back.");
            exception.printStackTrace();
        }
    }
    public Flower ChangeDetails(ChangeFlower flowerEdit) {
        Flower flower1 = null;
        try {
            session.beginTransaction();
            int id = flowerEdit.getId();
            flower1 = session.get(Flower.class, id);
            if (flower1 != null) {
                if(flowerEdit.getNewName() != null){
                    flower1.setFlowerName(flowerEdit.getNewName());
                }
                if(flowerEdit.getNewPrice() != null){
                    Double NewPrice = Double.parseDouble(flowerEdit.getNewPrice());
                    flower1.setPrice(NewPrice);
                }
                if(flowerEdit.getNewType() != null){
                    flower1.setType(flowerEdit.getNewType());
                }
                if(!flowerEdit.getNewBranches().isEmpty()){
                    for (int idBranch : flowerEdit.getNewBranches()){
                        Branch branch = session.get(Branch.class, idBranch);
                        if(!(flower1.getBranch().contains(branch))){
                            flower1.getBranch().add(branch);
                        }
                    }
                }
                if(flowerEdit.getImage() != null){
                    flower1.setImage(flowerEdit.getImage());
                }
                if(!flowerEdit.getRemoveBranches().isEmpty()){
                    for (int idBranch : flowerEdit.getRemoveBranches()){
                        Branch branch = session.get(Branch.class, idBranch);
                        if(flower1.getBranch().contains(branch)){
                            flower1.getBranch().remove(branch);
                        }
                    }
                }
                session.update(flower1);
            }
            session.getTransaction().commit();
            return flower1;
        }
        catch (Exception e) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        }
        return flower1;

    }
    public void PutSale(int id, int newSale){
        try{
            session.beginTransaction();
            Flower flower = session.get(Flower.class, id);
            if (flower != null) {
                flower.setSale(newSale);
                session.update(flower);
            }
            session.getTransaction().commit();
        }
        catch (Exception e) {
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
    public static LoginResponse loginCustomer(String username, String password) {
        try {
            session.beginTransaction();

            Customer customer = (Customer) session.createQuery(
                            "FROM Customer WHERE username = :username AND password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();

            session.getTransaction().commit();

            if (customer != null) {
                LoginResponse newLogIn = new LoginResponse(true, "Login successful!");
                newLogIn.setCustomer(customer);
                return newLogIn;
            } else {
                return new LoginResponse(false, "Incorrect username or password.");
            }
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return new LoginResponse(false, "Server error during login.");
        }
    }

    public static LoginResponse loginEmployee(String username, String password) {
        try {
            session.beginTransaction();

            Employee employee = (Employee) session.createQuery(
                            "FROM Employee WHERE username = :username AND password = :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .uniqueResult();

            session.getTransaction().commit();

            if (employee != null) {
                LoginResponse newLogin = new LoginResponse(true,"Employee login successful!");
                newLogin.setEmployee(employee);
                return newLogin;
            } else {
                return new LoginResponse(false,"Incorrect employee credentials.");
            }
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            return new LoginResponse(false, "Server error during employee login.");
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

    public void LogOutCustomer(int id) {
        try{
            session.beginTransaction();
            Customer customer = session.get(Customer.class, id);
            if (customer != null) {
                customer.setLoggedIn(false);
                session.update(customer);
                session.getTransaction().commit();
            }
            else {
                System.out.println("Customer with ID " + id + " not found.");
            }
        }
        catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            exception.printStackTrace();
        }
    }
    public void saveorder(Order order) {
        try {
            Customer user;
            session.beginTransaction();
            for(CartProduct product: order.getProducts()) {
                product.setOrder(order); // חובה – שיהיה קישור הפוך
                session.save(product);
            }
            session.save(order);
            user = session.get(Customer.class, order.getCustomer().getId());
            user.addOrder(order);
            session.update(user);
            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            else {
                System.err.println("An error occured on saving order.");
                exception.printStackTrace();
            }
        }

    }

    public void deleteorder(Order order) {
        try {
            if (!session.getTransaction().isActive()) {
                session.beginTransaction();
            }

            // טען מחדש את הלקוח אם צריך
            Customer user = session.get(Customer.class, order.getCustomer().getId());
            for (CartProduct product: order.getProducts()) {
                order.removeProduct(product);
                session.delete(product);
            }

            // ניתוק הקשר של ההזמנה מהלקוח (אם צריך)
            if (user != null) {
                user.removeOrder(order);
                session.update(user);
            }

            // מחיקת ההזמנה (Hibernate יטפל במוצרים הקשורים בזכות orphanRemoval)
            session.delete(order);

            session.getTransaction().commit();
        } catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            } else {
                System.err.println("An error occurred while deleting order:");
                exception.printStackTrace();
            }
        }
    }




    public void LogOutEmployee(int id) {
        try{
            session.beginTransaction();
            Employee employee = session.get(Employee.class, id);
            if (employee != null) {
                employee.setLogin(false);
                session.update(employee);
                session.getTransaction().commit();
            }
            else {
                System.out.println("employee with ID " + id + " not found.");
            }
        }
        catch (Exception exception) {
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            exception.printStackTrace();
        }
    }


    public static List<?> getAllEntities(String entityType) throws Exception {
        try {
            session.beginTransaction();

            List<?> result;

            if (entityType.equals("customer")) {
                System.out.println("Querying all customers...");
                result = session.createQuery("FROM Customer", Customer.class).getResultList();
                System.out.println("Customer query returned " + result.size() + " results.");
            } else {
                List<Employee> allEmployees = session.createQuery("FROM Employee", Employee.class).getResultList();

                result = switch (entityType) {
                    case "networkWorker" -> allEmployees.stream()
                            .filter(emp -> emp.getPermission() == 5)
                            .map(emp -> session.get(NetworkWorker.class, emp.getId()))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                    case "branchManager" -> allEmployees.stream()
                            .filter(emp -> emp.getPermission() == 3)
                            .map(emp -> session.get(BranchManager.class, emp.getId()))
                            .collect(Collectors.toList());
                    case "costumerService" -> allEmployees.stream()
                            .filter(emp -> emp.getPermission() == 2)
                            .map(emp -> session.get(CostumerServiceEmployee.class, emp.getId()))
                            .collect(Collectors.toList());
                    case "storeChainManager" -> allEmployees.stream()
                            .filter(emp -> emp.getPermission() == 4)
                            .map(emp -> session.get(StoreChainManager.class, emp.getId()))
                            .collect(Collectors.toList());
                    default -> throw new IllegalArgumentException("Unknown entity type: " + entityType);
                };
            }

            session.getTransaction().commit();
            return result;

        } catch (Exception e) {
            System.err.println("FAILED to get " + entityType);
            if (session != null && session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            e.printStackTrace();
            throw e;
        }
    }
    public static Object findUserByUsername(String role, String username) {
        try {
            session.beginTransaction();

            if (role.equals("customer")) {
                Customer customer = session.createQuery(
                                "FROM Customer WHERE username = :username", Customer.class)
                        .setParameter("username", username)
                        .uniqueResult();
                session.getTransaction().commit();
                return customer;
            }

            // Query the base Employee table
            Employee employee = session.createQuery(
                            "FROM Employee WHERE username = :username", Employee.class)
                    .setParameter("username", username)
                    .uniqueResult();

            session.getTransaction().commit();

            if (employee == null) {
                return null;
            }

            // Now determine subclass based on permission
            return switch (employee.getPermission()) {
                case 2 -> session.get(CostumerServiceEmployee.class, employee.getId());
                case 3 -> session.get(BranchManager.class, employee.getId());
                case 4 -> session.get(StoreChainManager.class, employee.getId());
                case 5 -> session.get(NetworkWorker.class, employee.getId());
                default -> employee; // Fallback: return base class if no match
            };

        } catch (Exception e) {
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
    }



    public static boolean isUsernameTakenIn(String role, String username) {
        try {
            session.beginTransaction();

            Long count;

            if (role.equals("customer")) {
                count = session.createQuery(
                                "SELECT COUNT(c) FROM Customer c WHERE c.username = :username", Long.class)
                        .setParameter("username", username)
                        .uniqueResult();
            } else {
                count = session.createQuery(
                                "SELECT COUNT(e) FROM Employee e WHERE e.username = :username", Long.class)
                        .setParameter("username", username)
                        .uniqueResult();
            }

            session.getTransaction().commit();
            return count != null && count > 0;

        } catch (Exception e) {
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            e.printStackTrace();
            return true; // Assume taken on error
        }
    }



    public static UpdateUserResponse updateUser(UpdateUserRequest request) {
        String role = request.getRole();
        String originalUsername = request.getOriginalUsername();
        String newUsername = role.equals("customer") ? request.getNewUsername() : request.getWorkerNewUsername();

        // Check if new username is taken by someone else
        if (!originalUsername.equals(newUsername) && isUsernameTakenIn(role, newUsername)) {
            return new UpdateUserResponse(false, "The new username is already taken.");
        }

        Object user = findUserByUsername(role, originalUsername);
        if (user == null) {
            return new UpdateUserResponse(false, "Original user not found.");
        }

        try {
            session.beginTransaction();

            if (user instanceof Customer ) {
                Customer customer = (Customer) user;

                if (!request.getNewUsername().isEmpty()) customer.setUsername(request.getNewUsername());
                if (!request.getFirstName().isEmpty()) customer.setFirstName(request.getFirstName());
                if (!request.getLastName().isEmpty()) customer.setLastName(request.getLastName());
                if (!request.getEmail().isEmpty()) customer.setEmail(request.getEmail());
                if (!request.getPhone().isEmpty()) customer.setPhone(request.getPhone());
                if (!request.getPassword().isEmpty()) customer.setPassword(request.getPassword());
                // credit card fields and others if needed
                session.update(customer);

            } else if (user instanceof Employee ) {
                Employee employee = (Employee) user;

                if (!request.getWorkerNewUsername().isEmpty()) employee.setUsername(request.getWorkerNewUsername());
                if (!request.getWorkerPassword().isEmpty()) employee.setPassword(request.getWorkerPassword());
                if (!request.getName().isEmpty()) employee.setName(request.getName());
                if (request.getPermision() != -1) employee.setPermission(request.getPermision());

                if (employee instanceof BranchManager ) {
                    BranchManager manager = (BranchManager) employee;
                    // Update branch only if a new branch is provided
                    if (request.getBranch() != null && !request.getBranch().isEmpty()) {
                        Branch newBranch = session.createQuery(
                                        "FROM Branch WHERE address = :address", Branch.class)
                                .setParameter("address", request.getBranch())
                                .uniqueResult();

                        if (newBranch != null && newBranch != manager.getBranch()) {
                            manager.setBranch(newBranch);
                        }
                    }
                }

                session.update(employee);
            }

            session.getTransaction().commit();
            return new UpdateUserResponse(true, "User updated successfully.");

        } catch (Exception e) {
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            e.printStackTrace();
            return new UpdateUserResponse(false, "Error updating user: " + e.getMessage());
        }
    }


    public static BlockUserResponse handleBlockUser(BlockUserRequest request) {
        String role = request.getRole();
        String username = request.getUsername();
        boolean block = request.isBlock();

        Object user = findUserByUsername(role, username);

        if (user == null) {
            return new BlockUserResponse(false, "User not found.");
        }

        try {
            session.beginTransaction();

            if (user instanceof Customer) {
                ((Customer) user).setBlocked(block);
                session.update(user);
            } else if (user instanceof Employee) {
                ((Employee) user).setBlocked(block);
                session.update(user);
            }

            session.getTransaction().commit();
            String msg = block ? "User blocked successfully." : "User unblocked successfully.";
            return new BlockUserResponse(true, msg);

        } catch (Exception e) {
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            e.printStackTrace();
            return new BlockUserResponse(false, "Error: " + e.getMessage());
        }
    }





}
