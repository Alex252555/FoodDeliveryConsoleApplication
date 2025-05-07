public class CartItem {

    private String dishName;
    private String restaurantName;
    private double price;
    private int amount;

    public CartItem(String dishName, String restaurantName, double price, int amount){
        if(amount<1 || price<=0){
            throw new IllegalArgumentException("Invalid amount or price, please enter amount and price bigger than 0");
        }
        this.dishName=dishName;
        this.restaurantName=restaurantName;
        this.price=price;
        this.amount=amount;
    }

    public String getName(){
        return dishName;
    }
    public String getRestaurantName(){
        return restaurantName;
    }
    public double getPrice(){
        return price;
    }
    public int getAmount(){
        return amount;
    }
}
