import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class VisualInterface {

    Socket s;
    private Scanner scan=new Scanner(System.in);
    private Scanner serverScanner;
    private PrintStream printout;
    private ArrayList<Profile> registeredProfiles=new ArrayList<Profile>();
    private ArrayList<Restaurant> registeredEstablishments=new ArrayList<Restaurant>();
    private ArrayList<Dish> menu=new ArrayList<Dish>();
    private ArrayList<Order> orders=new ArrayList<Order>();

    Profile currentProfile;

    public VisualInterface(Socket s) throws IOException {
        this.s=s;
        serverScanner=new Scanner(s.getInputStream());
        printout=new PrintStream(s.getOutputStream());
    }

    public void displaySettings(){
        System.out.println("--------------------");
        System.out.println("Enter 1 to log out\nEnter 2 to close app\nEnter 0 to go back");

        int option=scan.nextInt();
        scan.nextLine();

        switch (option){
            case 1:
                currentProfile=null;
                mainPage();
                break;
            case 2:
                return;
            case 0:
                mainPage();
        }
    }

    public void removeDish(){
        System.out.println("-------------------\nEnter dish index or 0 to go back:\n");
        printout.println(1);
        serverScanner.nextInt();
        serverScanner.nextLine();

        while(true){
            String message=serverScanner.nextLine();

            if(message.equals("")){
                break;
            }
            else{
                System.out.println(message);
            }
        }

        int option=scan.nextInt();
        scan.nextLine();

        if(option==0){
            profileCheck(currentProfile);
        }
        scan.nextLine();
        printout.println(11);
        printout.println(option);
        System.out.println(serverScanner.nextLine());
    }
    public void removeCartItem(){
        System.out.println("--------------------");
        System.out.println("Enter item number: ");
        int number=scan.nextInt();
        scan.nextLine();

        if(currentProfile.getCart().size()==0){
            System.out.println("No items in cart!");
            manageCart();
        }

        if(number<1 || number>currentProfile.getCart().size()){
            System.out.println("Invalid number! Please try again");
            removeCartItem();
        }
        else{
            currentProfile.getCart().remove(currentProfile.getCart().get(number-1));
            System.out.println("Item removed from cart!");
            manageCart();
        }
    }

    public void displayEstablishmentBudgets(){
        System.out.println("--------------------\nEnter 0 to go back\n");

        printout.println(14);

        while(true){
            String message=serverScanner.nextLine();
            if(message.equals("")==false){
                System.out.println(message);
            }
            else{
                break;
            }
        }
        managerPanel();
    }

    public void manageCart(){

        if(currentProfile==null){
            System.out.println("Please log in to be able to order!");
            mainPage();
        }
        if(currentProfile.getCart().size()==0){
            System.out.println("Your cart is empty!");
            mainPage();
        }

        System.out.println("--------------------\nYour cart items:");

        for(int i=0;i<currentProfile.getCart().size(); i++){
            System.out.println("Number: ["+Integer.toString(i+1)+"], Dish: "+currentProfile.getCart().get(i).getName()+", From restaurant: "+currentProfile.getCart().get(i).getRestaurantName()+", Price: "+currentProfile.getCart().get(i).getPrice()+" Amount: "+currentProfile.getCart().get(i).getAmount());
        }

        System.out.println("\nEnter 1 to create order\nEnter 2 to remove item\nEnter 0 to go back\n");
        int option=scan.nextInt();
        scan.nextLine();

        switch (option){
            case 0:
                mainPage(); break;
            case 2:
                removeCartItem(); break;
            case 1:

                double reward=0;

                for(int i=0; i<currentProfile.getCart().size(); i++){
                    String restaurantInfo=currentProfile.getCart().get(i).getRestaurantName()+"|"+(currentProfile.getCart().get(i).getPrice()*currentProfile.getCart().get(i).getAmount())*0.7;
                    printout.println(13);
                    printout.println(restaurantInfo);
                }

                for(int i=0; i<currentProfile.getCart().size(); i++){
                    reward+=currentProfile.getCart().get(i).getPrice()*currentProfile.getCart().get(i).getAmount();
                }

                String o=currentProfile.getUsername()+"|"+reward*0.3+"|"+"Pending";
                printout.println(7);
                printout.println(o);
                System.out.println(serverScanner.nextLine());
                currentProfile.getCart().clear();
                mainPage();
                break;
        }
    }

    public void orderDish(CartItem c){
        System.out.println("--------------------");
        int amount= c.getAmount();
        if(amount<=0){
            System.out.println("Invalid amount! Please try again");
            System.out.println("--------------------");
            orderDish(c);
        }
        else{
            System.out.println("Order added to cart!");
            currentProfile.addToCart(c);
            System.out.println("--------------------");
            displayMenu(null);
        }

    }

    public void takeOrder(){
        System.out.println("--------------------\nEnter order number to claim it. Enter 0 to go back");
        System.out.println("Your budget: "+currentProfile.getGeneratedBudget()+"\nOrders:\n");

        printout.println(9);

        while (true){
            String result=serverScanner.nextLine();
            System.out.println(result);
            if(result.equals("")){
                break;
            }
        }

        int option=scan.nextInt();
        scan.nextLine();

        if(option<=0){
            mainPage();
        }

        printout.println(10);
        printout.println(option-1);

        System.out.println(serverScanner.nextLine());

        System.out.println(serverScanner.nextLine());

        double budgetToAdd=serverScanner.nextDouble();
        serverScanner.nextLine();

        currentProfile.addToBudget(budgetToAdd);

        profileCheck(currentProfile);
    }

    public void displayMenu(String filter){
        if(filter==null){
            filter="null";
        }
        if(currentProfile==null){
            System.out.println("Please use your customer profile!");
            mainPage();
        }
        if(currentProfile.getRole().equals("Customer")==false){
            System.out.println("Please use your customer profile!");
            mainPage();
        }

        System.out.println("--------------------");
        System.out.println("Enter \"Dish\" to filter by dishes");
        System.out.println("Enter \"Appetizer\" to filter by appetizers");
        System.out.println("Enter \"Dessert\" to filter by desserts");
        System.out.println("Enter dish number to order\nEnter 0 to go back\n");

        printout.println(1);
        printout.println(filter);

        int listSize=serverScanner.nextInt();
        serverScanner.nextLine();

        if(listSize==0){
            System.out.println("No offers in the moment. Sorry!");
            mainPage();
        }

        while (true) {
            String line = serverScanner.nextLine();
            if (line.equals("")) {
                break;
            }
            System.out.println(line);
        }

        String option=scan.nextLine();

        if(option.equals("0")){
            mainPage();
        }
        else if(option.equals("Dish") || option.equals("Appetizer") || option.equals("Dessert")){
            filter=option;
            displayMenu(filter);
        }

        else{
            try{
                int menuChoice=Integer.parseInt(option);
            }
            catch (InputMismatchException e){
                System.out.println("Please enter a numerical value!");
                displayMenu(null);
            }
            System.out.println("Enter amount of the dish you would like or 0 to go back");
            int amount=scan.nextInt();
            scan.nextLine();

            if(amount==0){
                displayMenu(null);
            }

            printout.println(8);
            printout.println(Integer.parseInt(option)-1);
            String itemInfo=serverScanner.nextLine();
            String[] itemParts=itemInfo.split("\\|");
            CartItem c=new CartItem(itemParts[0],itemParts[1],Double.parseDouble(itemParts[2]),amount);

            System.out.println("Dish ordered!");
            orderDish(c);

        }
    }

    public void managerPanel(){
        int option;
        System.out.println("-------------------- \n");
        System.out.println("Enter 1 to add dish");
        System.out.println("Enter 2 to add establishment");
        System.out.println("Enter 3 to remove dishes");
        System.out.println("Enter 4 to remove establishments");
        System.out.println("Enter 5 to view establishment revenues");
        System.out.println("Enter 6 to go back");
        System.out.println("\n--------------------");

        option=scan.nextInt();
        scan.nextLine();

        switch (option){
            case 1: registerDish(); break;
            case 2: registerEstablishment(); break;
            case 3: removeDish();break;
            case 4: removeEstablishment(); break;
            case 5: displayEstablishmentBudgets(); break;
            case 6: profileCheck(currentProfile); break;
            default:profileCheck(currentProfile);
        }

    }

    public void registerDish(){
        System.out.println("--------------------\nPlease enter dish name or 0 to go back:");
        String name=scan.nextLine();

        if(name.equals("0")){
            profileCheck(currentProfile);
        }

        System.out.println("Please enter dish filter:");
        String filter=scan.nextLine();

        if(filter.equals("Dish")==false && filter.equals("Appetizer")==false && filter.equals("Dessert")==false){
            System.out.println("Please enter a valid filter!");
            registerDish();
        }

        System.out.println("Please enter dish description:");
        String description=scan.nextLine();

        System.out.println("Please enter restaurant name:");
        String restaurantName=scan.nextLine();

        System.out.println("Please enter restaurant address:");
        String restaurantAddress=scan.nextLine();

        System.out.println("Please enter dish price:");
        Double price=scan.nextDouble();
        scan.nextLine();

        printout.println(5);
        printout.println(new String(name+"|"+description+"|"+restaurantName+"|"+restaurantAddress+"|"+filter+"|"+price));

        int reply=serverScanner.nextInt();
        serverScanner.nextLine();

        switch (reply){
            case 1:
                System.out.println("Dish already exists!");
                mainPage();
                break;
            case 2:
                System.out.println("Restaurant not found! Please register it first");
                mainPage();
                break;
            case 3:
                System.out.println("Dish added successfully!");
                mainPage();
                break;
        }


    }
    public void registerEstablishment(){

        String name;
        String address;
        int rating;

        System.out.println("-------------------- \n");
        System.out.println("Enter establishment name: ");

        name=scan.nextLine();

        System.out.println("\nEnter establishment address: ");

        address=scan.nextLine();

        System.out.println("Enter establishment rating: ");

        rating=scan.nextInt();
        scan.nextLine();

        if(rating<1 || rating>10){
            System.out.println("Invalid rating! Please try again");
            registerEstablishment();
        }

        printout.println(2);

        String establishmentInfo=name+"|"+address+"|"+Integer.toString(rating);

        printout.println(establishmentInfo);

        System.out.println(serverScanner.nextLine());

        mainPage();

    }

    public void removeEstablishment(){
        System.out.println("--------------------\nEnter establishment name or 0 to go back:");
        String name=scan.nextLine();
        System.out.println("Enter establishment address:");
        String address=scan.nextLine();

        printout.println(6);
        printout.println(name+"|"+address);

        System.out.println(serverScanner.nextLine());
        mainPage();
    }
    public void createProfile(){
        String password;
        String repeatPassword;
        String username;
        String role;

        System.out.println("--------------------");
        System.out.println("Enter username: ");

        username=scan.nextLine();

        System.out.println();
        System.out.println("Enter password: ");

        password=scan.nextLine();

        System.out.println();
        System.out.println("Confirm password: ");

        repeatPassword=scan.nextLine();

        if(password.equals(repeatPassword)==false){
            System.out.println("Passwords don't match! Please try again");
            createProfile();
        }

        System.out.println();
        System.out.println("Choose your role: Customer | Delivery | Manager");

        role=scan.nextLine();

        if(role.equals("Customer")==false && role.equals("Manager")==false && role.equals("Delivery")==false){
            System.out.println("Please enter a valid role!");
            createProfile();
        }

        String message=username+"|"+password+"|"+role;

        printout.println(3);
        printout.println(message);

        String reply=serverScanner.nextLine();
        System.out.println(reply);

        if(reply.equals("Account successfully created!")){
            Profile p=new Profile(username,password,role);
            currentProfile=p;
        }

        mainPage();

    }

    public void logIn(){
        System.out.println("--------------------\nPlease enter username or enter 0 to go back");
        String username=scan.nextLine();
        System.out.println("Please enter password or enter 0 to go back");
        String password=scan.nextLine();
        String commandInfo=new String(username+"|"+password);

        printout.println(4);
        printout.println(commandInfo);

        String reply=serverScanner.nextLine();

        if(reply.equals("true")){
            System.out.println(serverScanner.nextLine());
            String role=serverScanner.nextLine();
            Profile p=new Profile(username,password,role);
            currentProfile=p;
            mainPage();
        }
        else{
            System.out.println(serverScanner.nextLine());
            mainPage();
        }

    }
    public void profileCheck(Profile p){
        if(p==null){
            System.out.println("Not logged in! Would you like to log in or create an account? \n Enter 1 for log in \n Enter 2 for sign up \n Enter 3 to go back");

            int option=scan.nextInt();
            scan.nextLine();

            switch (option){
                case 1:
                    logIn();
                    break;
                case 2:
                    createProfile();
                    break;
                case 3:
                    mainPage();
                    break;
                default:
                    mainPage();
            }
        }
        else{
            int option;
            String oldPassword;
            String newPassword;

            System.out.println("--------------------");
            System.out.println("Your profile information: \n Username: "+currentProfile.getUsername()+"\n Role: "+currentProfile.getRole());

            System.out.println("\nEnter 1 to change your password");

            if(currentProfile.getRole().equals("Delivery")){
                System.out.println("\nEnter 2 to view pending orders and your budget");
            }
            else if(currentProfile.getRole().equals("Manager")){
                System.out.println("\nEnter 2 to manage dishes and establishments");
            }

            System.out.println("\nEnter 3 to go back to home page");
            
            option=scan.nextInt();
            scan.nextLine();

            switch(option){
                case 1:
                    System.out.println("Please confirm your current password: ");

                    oldPassword=scan.nextLine();

                    if(oldPassword.equals(currentProfile.getPassword())){}
                    else{
                        System.out.println("Incorrect password! Please try again");
                        profileCheck(currentProfile);
                    }

                    System.out.println("Please enter a new password: ");

                    newPassword=scan.nextLine();

                    if(oldPassword==newPassword){
                        System.out.println("Please choose a different password");
                    }
                    else{
                        currentProfile.changePassword(oldPassword,newPassword);
                        printout.println(12);
                        printout.println(currentProfile.getUsername()+"|"+currentProfile.getPassword());
                        profileCheck(currentProfile);
                    }

                    break;
                case 2:
                    if(currentProfile.getRole().equals("Delivery")){
                        takeOrder();
                    }
                    else if(currentProfile.getRole().equals("Manager")){
                        managerPanel();
                    }
                    else{
                        System.out.println("Invalid choice, please try again \n");
                        profileCheck(currentProfile);
                    }
                    break;
                case 3:
                    mainPage();
                    break;
                default:
                    System.out.println("Invalid choice, please try again \n");
                    profileCheck(currentProfile);
                    break;
            }
        }
    }

    public void mainPage(){

        int option;

        System.out.println("--------------------");
        System.out.println();
        System.out.println("Hello! Welcome to SesameSoup");
        System.out.println();
        System.out.println("Enter 1 to view offers");
        System.out.println("Enter 2 to view your profile");
        System.out.println("Enter 3 to view your cart");
        System.out.println("Enter 4 to settings");

        while(true){

            option=scan.nextInt();
            scan.nextLine();

            if(option!=1 && option!=2 && option!=3 && option!=4){
                System.out.println("Incorrect input. Please try again");
            }
            else {
                break;
            }
        }

        switch (option){
            case 1: displayMenu(null); break;
            case 2: profileCheck(currentProfile);break;
            case 3: manageCart(); break;
            case 4: displaySettings(); break;
            default: mainPage();
        }
    }
}
