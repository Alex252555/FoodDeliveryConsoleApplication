import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerThread extends Server implements Runnable{

    private Socket socket;
    private ArrayList<Profile> profiles;
    private ArrayList<Dish> dishes;
    private ArrayList<Restaurant> restaurants;
    private ArrayList<Order> orders;
    private DatabaseConnection db;
    private Connection c;

    public ServerThread(Socket s, ArrayList<Profile> profiles, ArrayList<Dish> dishes, ArrayList<Restaurant> restaurants, ArrayList<Order> orders, DatabaseConnection db, Connection c){
        this.socket = s;
        this.profiles = profiles;
        this.dishes = dishes;
        this.restaurants = restaurants;
        this.orders = orders;
        this.db=db;
        this.c=c;
    }

    @Override
    public void run() {
        try {
            Scanner scan=new Scanner(socket.getInputStream());
            PrintStream printout = new PrintStream(socket.getOutputStream());

            while (true){
                int command=scan.nextInt();
                scan.nextLine();
                System.out.println("command received");

                switch (command){
                    case 1:
                        printout.println(dishes.size());
                        System.out.println(dishes.size());
                        for(int i=0; i<dishes.size(); i++){
                            System.out.println("here");
                            printout.println(new String(Integer.toString(i+1)+": "+dishes.get(i).getName()+", Filter: ")+dishes.get(i).getFilter()+", Description: "+dishes.get(i).getDescription()+", Restaurant: "+dishes.get(i).getRestaurant()+", Price: "+Double.toString(dishes.get(i).getPrice()));
                        }
                        break;

                    case 2:
                        boolean establishmentFound=false;
                        String establishment=scan.nextLine();
                        String[] parts=establishment.split("\\|");
                        String name = parts[0];
                        String address = parts[1];
                        int rating = Integer.parseInt(parts[2]);
                        System.out.println(name+" "+address);

                        for(int i=0; i<restaurants.size(); i++){
                            System.out.println(restaurants.get(i).getName()+" "+restaurants.get(i).getAddress());
                            if (restaurants.get(i).getName().equals(name) && restaurants.get(i).getAddress().equals(address)){
                                System.out.println("found");
                                establishmentFound=true;
                                break;
                            }
                        }

                        if(establishmentFound==true){
                            System.out.println("sending error");
                            printout.println("Establishment already exists!");
                        }
                        else {
                            db.addEstablishment(c,name,address,rating);
                            printout.println("Establishment successfully added!");
                            db.loadTables(c);
                        }
                        break;

                    case 3:
                        boolean profileFound=false;
                        String profile=scan.nextLine();
                        String[] profileParts=profile.split("|");
                        String username=profileParts[0];
                        String password=profileParts[1];
                        String role=profileParts[2];

                        for(int i=0; i< profiles.size(); i++){
                            if(profiles.get(i).getUsername().equals(username)){
                                profileFound=true;
                                break;
                            }
                        }

                        if(profileFound==true){
                            printout.println("Username already exists");
                        }
                        else{
                            db.addProfile(c,username,password,role);
                            db.loadTables(c);
                            printout.println("Account successfully created!");
                        }
                        break;
                    case 4:
                        boolean logInFound=false;
                        String logInProfile=scan.nextLine();
                        String[] logInParts=logInProfile.split("\\|");
                        System.out.println(logInParts[0]);
                        String logInUser=logInParts[0];
                        String logInPassword=logInParts[1];
                        String roleFound=null;

                        for(int i=0; i<profiles.size(); i++){
                            System.out.println(profiles.get(i).getUsername()+" "+logInUser);
                            System.out.println(profiles.get(i).getPassword()+" "+logInPassword);
                            if(profiles.get(i).getUsername().equals(logInUser) && profiles.get(i).getPassword().equals(logInPassword)){
                                logInFound=true;
                                roleFound=profiles.get(i).getRole();
                                break;
                            }
                        }

                        if(logInFound==true){
                            printout.println(true);
                            printout.println("Log in successful!");
                            printout.println(roleFound);
                        }
                        else{
                            System.out.println("Sending");
                            printout.println(false);
                            printout.println("Wrong password or profile not found!");
                        }
                        break;
                    case 5:
                        boolean restaurantFound=false;

                        String dishInfo=scan.nextLine();
                        String[] dishParts=dishInfo.split("\\|");

                        String dishName=dishParts[0];
                        String dishDesc=dishParts[1];
                        String restaurantName=dishParts[2];
                        String restaurantAddress=dishParts[3];
                        String filter=dishParts[4];
                        Double price=Double.parseDouble(dishParts[5]);
                        boolean resFound=false;

                        int message=0;

                        for(int i=0; i<dishes.size(); i++){
                            if(dishes.get(i).getName().equals(dishName)&&dishes.get(i).getRestaurant().getName().equals(restaurantName)&&dishes.get(i).getRestaurant().getAddress().equals(restaurantAddress)){
                                message=1;
                                break;
                            }
                        }

                        if(message==0){
                            for(int i=0;i<restaurants.size(); i++){
                                if(restaurants.get(i).getName().equals(restaurantName)&&restaurants.get(i).getAddress().equals(restaurantAddress)){
                                    resFound=true;
                                    break;
                                }
                            }
                        }

                        if(resFound==false && message==0){
                            message=2;
                        }

                        if(message==0){
                            db.addDish(c,dishName,dishDesc,restaurantName,restaurantAddress,filter,price);
                            message=3;
                        }

                        printout.println(message);
                        break;

                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
