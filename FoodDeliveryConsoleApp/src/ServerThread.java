import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerThread implements Runnable{

    private Socket socket;

    public ServerThread(Socket s){
        this.socket = s;

    }

    @Override
    public void run() {
        try {
            Scanner scan=new Scanner(socket.getInputStream());
            PrintStream printout = new PrintStream(socket.getOutputStream());

            while (true){

                int command=scan.nextInt();
                System.out.println("Received command: " + command); // <-- ADD THIS
                scan.nextLine();

                switch (command){
                    case 1: //Print menu
                        printout.println(Server.menu.size());
                        System.out.println(Server.menu.size());
                        for(int i=0; i<Server.menu.size(); i++){
                            printout.println(new String(Integer.toString(i+1)+": "+Server.menu.get(i).getName()+", Filter: ")+Server.menu.get(i).getFilter()+", Description: "+Server.menu.get(i).getDescription()+", Restaurant name: "+Server.menu.get(i).getRestaurant().getName()+", Restaurant address: "+Server.menu.get(i).getRestaurant().getAddress()+", Price: "+Double.toString(Server.menu.get(i).getPrice()));
                        }
                        printout.println("");
                        break;

                    case 2: //Add establishment
                        boolean establishmentFound=false;
                        String establishment=scan.nextLine();
                        String[] parts=establishment.split("\\|");
                        String name = parts[0];
                        String address = parts[1];
                        int rating = Integer.parseInt(parts[2]);
                        System.out.println(name+" "+address);

                        for(int i=0; i<Server.registeredEstablishments.size(); i++){
                            System.out.println(Server.registeredEstablishments.get(i).getName()+" "+Server.registeredEstablishments.get(i).getAddress());
                            if (Server.registeredEstablishments.get(i).getName().equals(name) && Server.registeredEstablishments.get(i).getAddress().equals(address)){
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
                            Server.d.addEstablishment(Server.c,name,address,rating);
                            printout.println("Establishment successfully added!");
                            Server.d.loadTables(Server.c);
                        }
                        break;

                    case 3: //Register profile
                        boolean profileFound=false;

                        String profile=scan.nextLine();
                        String[] profileParts=profile.split("\\|");
                        String username=profileParts[0];
                        String password=profileParts[1];
                        String role=profileParts[2];

                        for(int i=0; i< Server.registeredProfiles.size(); i++){
                            if(Server.registeredProfiles.get(i).getUsername().equals(username)){
                                profileFound=true;
                                break;
                            }
                        }

                        if(profileFound==true){
                            printout.println("Username already exists");
                        }
                        else{
                            printout.println("Account successfully created!");
                            Server.d.addProfile(Server.c,username,password,role);
                            Server.d.loadTables(Server.c);

                        }
                        break;

                    case 4: //Log in
                        boolean logInFound=false;
                        String logInProfile=scan.nextLine();
                        String[] logInParts=logInProfile.split("\\|");
                        String logInUser=logInParts[0];
                        String logInPassword=logInParts[1];
                        String roleFound=null;

                        for(int i=0; i<Server.registeredProfiles.size(); i++){
                            System.out.println(Server.registeredProfiles.get(i).getUsername()+" "+logInUser);
                            System.out.println(Server.registeredProfiles.get(i).getPassword()+" "+logInPassword);
                            if(Server.registeredProfiles.get(i).getUsername().equals(logInUser) && Server.registeredProfiles.get(i).getPassword().equals(logInPassword)){
                                logInFound=true;
                                roleFound=Server.registeredProfiles.get(i).getRole();
                                break;
                            }
                        }

                        if(logInFound==true){
                            printout.println("true");
                            printout.println("Log in successful!");
                            printout.println(roleFound);
                        }
                        else{
                            printout.println("false");
                            printout.println("Wrong password or profile not found!");
                        }
                        break;
                    case 5: //Add dish
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

                        for(int i=0; i<Server.menu.size(); i++){
                            if(Server.menu.get(i).getName().equals(dishName) && Server.menu.get(i).getRestaurant().getName().equals(restaurantName)&&Server.menu.get(i).getRestaurant().getAddress().equals(restaurantAddress)){
                                message=1;
                                break;
                            }
                        }

                        if(message==0){
                            for(int i=0;i<Server.registeredEstablishments.size(); i++){
                                if(Server.registeredEstablishments.get(i).getName().equals(restaurantName) && Server.registeredEstablishments.get(i).getAddress().equals(restaurantAddress)){
                                    resFound=true;
                                    break;
                                }
                            }
                        }

                        if(resFound==false && message==0){
                            message=2;
                        }

                        if(message==0){
                            System.out.println(dishName+" "+dishDesc+" "+restaurantName+" "+restaurantAddress+" "+filter+" "+price);
                            Server.d.addDish(Server.c,dishName,dishDesc,restaurantName,restaurantAddress,filter,price);
                            Server.d.loadTables(Server.c);
                            message=3;
                        }
                        System.out.println(message);
                        printout.println(message);
                        break;

                    case 6: //Delete establishment
                        String establishmentInfo=scan.nextLine();
                        String[] establishmentParts=establishmentInfo.split("\\|");
                        String estName=establishmentParts[0];
                        String estAddress=establishmentParts[1];
                        boolean deleted=false;

                        for(int i=0; i<Server.menu.size(); i++){
                            if(Server.menu.get(i).getRestaurant().getName().equals(estName) && Server.menu.get(i).getRestaurant().getAddress().equals(estAddress)){
                                System.out.println(Server.c+" "+Server.menu.get(i).getName()+" "+Server.menu.get(i).getRestaurant().getName()+" "+ Server.menu.get(i).getRestaurant().getAddress());
                                Server.d.removeDish(Server.c,Server.menu.get(i).getName(),Server.menu.get(i).getRestaurant().getName(), Server.menu.get(i).getRestaurant().getAddress());
                            }
                        }
                        Server.d.loadTables(Server.c);

                        for(int i=0; i<Server.registeredEstablishments.size(); i++){
                            if(estName.equals(Server.registeredEstablishments.get(i).getName())&&estAddress.equals(Server.registeredEstablishments.get(i).getAddress())){
                                deleted=true;
                                Server.d.removeEstablishment(Server.c,estName,estAddress);
                                Server.d.loadTables(Server.c);
                            }
                        }

                        if(deleted==true){
                            printout.println("Establishment removed successfully!");
                        }
                        else{
                            printout.println("Establishment not found!");
                        }
                        break;
                    case 7: //Create an order
                        String orderInfo=scan.nextLine();

                        String infoParts[]=orderInfo.split("\\|");

                        String orderUsername=infoParts[0];
                        String orderReward=infoParts[1];
                        String orderStatus="Pending";

                        System.out.println(orderReward);

                        if(Double.parseDouble(orderReward)==0){
                            printout.println("Order failed. Please try again");
                        }
                        else{
                            printout.println("Order successful");
                            Server.d.addOrder(Server.c,orderUsername,Double.parseDouble(orderReward),orderStatus);
                            Server.d.loadTables(Server.c);
                        }
                        break;
                    case 8: //Find ordered item in menu

                        int itemNumber=scan.nextInt();
                        scan.nextLine();

                        Dish d=Server.menu.get(itemNumber);
                        String itemInfo=d.getName()+"|"+d.getRestaurant().getName()+"|"+Double.toString(d.getPrice());
                        printout.println(itemInfo);
                        break;

                    case 9: //Print orders list

                        for(int i=0; i<Server.orders.size(); i++){
                            printout.println(new String("From: "+Server.orders.get(i).getUsername()+", Paying: "+Double.toString(Server.orders.get(i).getOrderDeliveryReward())+", Status: "+Server.orders.get(i).getOrderStatus()));
                        }
                        printout.println("");

                        break;

                    case 10: //Take order as a delivery man
                        int orderNumber=scan.nextInt();
                        scan.nextLine();

                        boolean orderAvailable=true;

                        String reply="Budget updated failed";

                        if(Server.orders.get(orderNumber).getOrderStatus().equals("Delivered")){
                            orderAvailable=false;
                        }

                        if(orderAvailable==true){
                            Server.d.changeOrderStatus(Server.c, orderNumber+1);
                            Server.d.loadTables(Server.c);
                            System.out.println("Order taken!");
                            printout.println("Order taken successfully");

                            reply="Budget updated successfully!";

                            printout.println(reply);
                            printout.println(Server.orders.get(orderNumber).getOrderDeliveryReward());
                        }
                        else{
                            printout.println("Order not available!");
                            printout.println(reply);
                            printout.println(0);
                        }
                        break;
                    case 11: //Remove dish
                        int dishToRemove=scan.nextInt();
                        scan.nextLine();

                        boolean removed=false;

                        if(dishToRemove<1 || dishToRemove>Server.menu.size()){
                            printout.println("Invalid dish number!");
                        }
                        else{
                            Dish dtr=Server.menu.get(dishToRemove-1);
                            Server.d.removeDish(Server.c,dtr.getName(),dtr.getRestaurant().getName(),dtr.getRestaurant().getAddress());
                            Server.d.loadTables(Server.c);

                            printout.println("Dish removed successfully!");

                        }
                        break;
                    case 12:
                        String profileInfo=scan.nextLine();
                        String[] profileInfoParts=profileInfo.split("\\|");
                        String userIdentification=profileInfoParts[0];
                        String newPassword=profileInfoParts[1];
                        Server.d.changeUserPassword(Server.c,userIdentification,newPassword);
                        Server.d.loadTables(Server.c);
                        break;
                }


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
