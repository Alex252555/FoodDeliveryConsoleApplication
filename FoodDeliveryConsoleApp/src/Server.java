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

    public static ArrayList<Profile> registeredProfiles=new ArrayList<Profile>();
    public static ArrayList<Restaurant> registeredEstablishments=new ArrayList<Restaurant>();
    public static ArrayList<Dish> menu=new ArrayList<Dish>();
    public static ArrayList<Order> orders = new ArrayList<Order>();
    public static DatabaseConnection d = new DatabaseConnection();
    public static Connection c;

    static {
        try {
            c = d.setUpConnection();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Server() throws SQLException, ClassNotFoundException {
    }

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        int number;

        ServerSocket ss = new ServerSocket(1211);

        d.loadTables(c);

        ExecutorService executor = Executors.newFixedThreadPool(3);

        while(true)
        {
            Socket socket = ss.accept();
            executor.execute(new ServerThread(socket));
        }
    }
}