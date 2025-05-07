public class Dish {
    private String name;
    private String description;
    private Restaurant restaurant;
    private String filter;
    private double price;

    public String getName(){
        return name;
    }
    public String getFilter(){
        return filter;
    }
    public String getDescription(){
        return description;
    }
    public double getPrice(){
        return price;
    }

    public Restaurant getRestaurant(){
        return restaurant;
    }

    public Dish(String name, String description, Restaurant restaurant, String filter,double price){
        this.name=name;
        this.description=description;
        this.restaurant=restaurant;
        this.filter=filter;
        this.price=price;
    }
}
