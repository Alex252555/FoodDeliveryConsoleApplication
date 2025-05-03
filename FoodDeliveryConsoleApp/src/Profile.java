import java.util.ArrayList;

public class Profile {

    private String username;
    private String password;
    private String role;
    private double generatedBudget=0;
    private ArrayList<CartItem> cart=new ArrayList<CartItem>();

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getRole(){
        return role;
    }

    public ArrayList<CartItem> getCart(){
        return cart;
    }

    public double getGeneratedBudget(){
        return generatedBudget;
    }

    public void addToBudget(double budget){
        this.generatedBudget+=budget;

        //Добавен бонус 10% ако доставчикът е генерирал определен оборот
        if(getGeneratedBudget()>50){
            this.generatedBudget+=(budget*0.1);
        }
    }

    public Profile(String username, String password, String role){
        this.username=username;
        this.password=password;
        this.role=role;
    }

    public Profile(){}

    public void changePassword(String oldPassword, String newPassword){
        if(oldPassword.equals(this.getPassword())){
            this.password=newPassword;
            System.out.println("Password changed successfully!");
        }
        else{
            System.out.println("Wrong password! Please try again");
        }
    }

    public void addToCart(CartItem c){
        cart.add(c);
    }
}
