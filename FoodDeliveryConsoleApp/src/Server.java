import javax.swing.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static ArrayList<Profile> registeredProfiles=new ArrayList<Profile>();
    private static ArrayList<Restaurant> registeredEstablishments=new ArrayList<Restaurant>();
    private static ArrayList<Dish> menu=new ArrayList<Dish>();
    private static ArrayList<Order> orders = new ArrayList<Order>();

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        int number;

        ServerSocket ss = new ServerSocket(1211);
        

        DatabaseConnection d = new DatabaseConnection();

        Connection c=d.setUpConnection();

        d.loadTables(c);

        registeredProfiles=d.getProfiles();
        registeredEstablishments=d.getRegisteredEstablishments();
        menu=d.getMenu();
        orders=d.getOrders();
        ExecutorService executor = Executors.newFixedThreadPool(3);

        while(true)
        {
            Socket socket = ss.accept();
            executor.execute(new ServerThread(socket,registeredProfiles,menu,registeredEstablishments,orders,d,c));
        }
    }
}