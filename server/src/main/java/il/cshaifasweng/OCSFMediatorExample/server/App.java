package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;


import java.util.List;
import java.util.Random;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

import il.cshaifasweng.OCSFMediatorExample.entities.Flower;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
/**
 * Hello world!
 *
 */
public class App
{

    private static SimpleServer server;
    public static DataBaseManagement instance = new DataBaseManagement();

    public static void main( String[] args ) throws IOException
    {
        server = new SimpleServer(3000);
        instance.initDataBase();
        server.listen();
    }
}
