import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
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
            System.out.println("Number: ["+Integer.toString(i+1)+"], Dish: "+currentProfile.getCart().get(i).getDish()+" Amount: "+currentProfile.getCart().get(i).getAmount());
        }

        System.out.println("\nEnter 1 to create order\nEnter 2 to remove item\nEnter 0 to go back\n");
        int option=scan.nextInt();

        switch (option){
            case 0:
                displayMenu(); break;
            case 2:
                removeCartItem(); break;
            case 1:
                //  ADD ORDER INTO GLOBAL ORDERS ARRAY FOR ALL USERS
        }
    }

    public void orderDish(Dish d){
        System.out.println("--------------------");
        System.out.println("Ordering dish: "+d.getName()+"\nPlease enter an amount to order or enter \"0\" to go back:");
        int amount=scan.nextInt();
        scan.nextLine();

        if(amount<0){
            System.out.println("Invalid amount! Please try again");
            System.out.println("--------------------");
            orderDish(d);
        }
        else if(amount==0){
            System.out.println("--------------------");
            displayMenu();
        }
        else{
            System.out.println("Order added to cart!");
            CartItem c=new CartItem(d,amount);
            currentProfile.addToCart(c);
            System.out.println("--------------------");
            displayMenu();
        }

    }

    public void displayMenu(){

        System.out.println("--------------------");
        System.out.println("Enter \"Dish\" to filter by dishes");
        System.out.println("Enter \"Appetizer\" to filter by appetizers");
        System.out.println("Enter \"Dessert\" to filter by desserts\n");

        printout.println(1);

        int listSize=serverScanner.nextInt();
        serverScanner.nextLine();

        if(listSize==0){
            System.out.println("No offers in the moment. Sorry!");
            mainPage();
        }

        while(serverScanner.nextLine()!=null){
            System.out.println(serverScanner.nextLine());
        }

        System.out.println("Enter dish number to order\nEnter 0 to go back");

        int option=scan.nextInt();

        if(option<0 ||(option>menu.size())){
            System.out.println("Invalid number! Please try again");
            displayMenu();
        }
        else if (option==0){
            mainPage();
        }
        else{
            for(int i=0;i<menu.size(); i++){
                if(i==option-1){
                    orderDish(menu.get(i));
                    break;
                }
            }
        }
    }

    public void managerPanel(){
        int option;
        System.out.println("-------------------- \n");
        System.out.println("Enter 1 to add dish");
        System.out.println("Enter 2 to add establishment");
        System.out.println("Enter 3 to remove dishes");
        System.out.println("Enter 4 to remove establishments");
        System.out.println("Enter 5 to go back");
        System.out.println("\n--------------------");

        option=scan.nextInt();
        scan.nextLine();

        switch (option){
            case 1: registerDish(); break;
            case 2: registerEstablishment(); break;
            case 3: break;
            case 4: break;
            case 5: profileCheck(currentProfile);
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

        printout.println(5);
        printout.println(new String(name+"|"+description+"|"+restaurantName+"|"+restaurantAddress+"|"+filter+"|"+price));

        int reply=serverScanner.nextInt();

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
        System.out.println(serverScanner.nextLine());

        mainPage();

    }
    public void createProfile(){
        String password;
        String repeatPassword;
        String username;
        int role;
        boolean available;

        System.out.println("--------------------");
        System.out.println("Enter username: ");

        username=scan.nextLine();

        for(int i=0; i<registeredProfiles.size(); i++){
            if(username.equals(registeredProfiles.get(i).getUsername())){
                System.out.println("Username unavailable! Please try again");
                createProfile();
            }
        }

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
        System.out.println("Choose your role: \n Press 1 for customer \n Press 2 for delivery \n Press 3 for manager");

        role=scan.nextInt();
        scan.nextLine();

        Profile p=new Profile();

        switch (role){
            case 1:
                p=new Profile(username,password,"Customer");
                break;
            case 2:
                p=new Profile(username,password,"Delivery");
                break;
            case 3:
                p=new Profile(username,password,"Manager");
                break;
            default:
                System.out.println("Invalid role! Please try again");
                createProfile();
        }

        System.out.println("Profile created successfully!");
        registeredProfiles.add(p);
        currentProfile=p;

        mainPage();
    }

    public void logIn(){
        System.out.println("--------------------\nPlease enter username or enter 0 to go back");
        String username=scan.nextLine();
        System.out.println("Please enter password or enter 0 to go back");
        String password=scan.nextLine();
        String commandInfo=new String(username+"|"+password);
        printout.println(4);
        System.out.println(commandInfo);
        printout.println(commandInfo);

        boolean reply=serverScanner.nextBoolean();
        serverScanner.nextLine();
        if(reply==true){
            System.out.println(serverScanner.nextLine());
            Profile p=new Profile(username,password,serverScanner.nextLine());
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

            System.out.println("\n Enter 1 to change your password");

            if(currentProfile.getRole()=="Delivery"){
                System.out.println("\n Enter 2 to view pending orders");
            }
            else if(currentProfile.getRole()=="Manager"){
                System.out.println("\n Enter 2 to manage dishes and establishments");
            }

            System.out.println("\n Enter 3 to go back to home page");
            
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
                        System.out.println("Password changed successfully!");
                        profileCheck(currentProfile);
                    }

                    break;
                case 2:
                    if(currentProfile.getRole()=="Delivery"){

                    }
                    else if(currentProfile.getRole()=="Manager"){
                        managerPanel();
                    }
                    else{
                        System.out.println("Invalid choice, please try again \n");
                        profileCheck(currentProfile);
                    }
                    break;
                case 3:
                    mainPage();
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
            case 1: displayMenu(); break;
            case 2: profileCheck(currentProfile);break;
            case 3: manageCart(); break;
            case 4: displaySettings(); break;
            default: mainPage();
        }
    }
}
