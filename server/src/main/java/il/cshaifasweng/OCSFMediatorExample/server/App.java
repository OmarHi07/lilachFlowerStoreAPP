package il.cshaifasweng.OCSFMediatorExample.server;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App
{

    private static SimpleServer server;
    public static DataBaseManagement instance = new DataBaseManagement();

    public static void main( String[] args ) throws IOException
    {    DataBaseManagement.insertBranchesWithComplaints();

        DataBaseManagement.insertDummyBranches();

        DataBaseManagement.insertDummyComplaints();
        server = new SimpleServer(3001);
        instance.initDataBase();
        server.listen();
        System.out.println("Server listening on port 3001");
    }
}
